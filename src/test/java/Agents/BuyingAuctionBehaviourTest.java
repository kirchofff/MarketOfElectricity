package Agents;
import ClassXML.*;
import ConsumerAgent.GetRequest;
import ConsumerAgent.SendRequestForEnergyBehaviour;
import DF.DfHelper;
import DistributerAgent.DistributeFSM;
import ProducerAgent.GenerationFSM;
import Topic.SendToTopic;
import additionPacakge.CheckHour;
import additionPacakge.CreateTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
@Slf4j

class BuyingAuctionBehaviourTest {
    private JadeTestingKit kit = new JadeTestingKit();
    private GetRequestWithPriceBehaviour inner;
    private CFG cfg = ParseXML.ParseXML("BootFactory");
    private CFGGeneration cfg1 = ParseGenerationXML.ParseXML("SES");
    private CFGGeneration cfg2 = ParseGenerationXML.ParseXML("WES");
    private CFGGeneration cfg3 = ParseGenerationXML.ParseXML("TES");
    private CheckHour checkHour = new CheckHour(System.currentTimeMillis(), 5000);
    private Functions functions;

    @BeforeEach
    void beforeEach(){
        kit.startJade();
    }

    @Test
    @SneakyThrows
    void successAuction1(){
        kit.createAgent("BootFactory", SendRequestBehaviour(20, cfg, checkHour, "BootFactory"));
        kit.createAgent("Distributor_Of_BootFactory", GetRequestForEnergyBehaviour());
        kit.createAgent("WES", SendToTopicPrice (cfg2, checkHour));
        kit.createAgent("SES", SendToTopicPrice (cfg1, checkHour));
        kit.createAgent("TES", SendToTopicPrice (cfg3, checkHour));
        Thread.sleep(1500);
        Assertions.assertEquals(1, inner.onEnd());
    }
    public Behaviour SendRequestBehaviour(double price, CFG cfg, CheckHour currentTime, String distributorName){
        return new OneShotBehaviour() {
            @Override
            public void action() {
                for (ParametersOfConsumer poc : cfg.getPeriods()) {
                    if (poc.getTime() == currentTime.returnCurrentTime()) {
                        ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
                        AID aid = new AID("Distributor_Of_BootFactory", false);
                        m.addReceiver(aid);
                        m.setContent((poc.getLoad() * cfg.getFullLoad()) + ";" + currentTime.returnCurrentTime() + ";" + price);
                        m.setProtocol("request");
                        log.info("distributor sent msg to {}", "Distributor_Of_" + distributorName);
                        myAgent.send(m);
                        myAgent.addBehaviour(new GetRequestWithPriceBehaviour(price));
                    }
                }
            }
        };
    }

    public Behaviour GetRequestForEnergyBehaviour() {
        return new OneShotBehaviour() {
            @Override
            public void onStart() {
                log.info("{} was born", myAgent.getLocalName());
            }

            @Override
            public void action() {
                MessageTemplate mt;
                List<AID> seller = new ArrayList<>();
                mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("request"));
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    if (msg.getProtocol().equals("request")) {
                        CreateTopic topic = new CreateTopic(myAgent);
                        AID subscribeOnTopic = topic.returnTopicName(myAgent.getLocalName());
                        ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
                        seller.forEach(request::addReceiver);
                        request.setProtocol("topic");
                        request.setContent(subscribeOnTopic.getLocalName());
                        myAgent.send(request);
                        log.debug("{} needs this amount of energy {} at {} hours peak price {}",
                                msg.getSender().getLocalName(),
                                msg.getContent().split(";")[0],
                                msg.getContent().split(";")[1],
                                msg.getContent().split(";")[2]);
                        myAgent.addBehaviour(new DistributeFSM(
                                myAgent,
                                Double.parseDouble(msg.getContent().split(";")[0]),
                                Double.parseDouble(msg.getContent().split(";")[2]),
                                subscribeOnTopic,
                                msg.getSender(),
                                seller));
                    } else block();
                }
            }
        };
    }


    public Behaviour SendToTopicPrice(CFGGeneration cfg, CheckHour time) {
            return new OneShotBehaviour() {
                private List<Double> coefficients = new ArrayList<>();

                @Override
                public void onStart() {
                    DfHelper.registerAgent(myAgent, "Seller");
                    log.info("{} was born", myAgent.getLocalName());
                    for (ParametersOfGeneration parameters : cfg.getCoefficients()) {
                        coefficients.add(parameters.getCoef());
                    }
                    Functions functions = new Functions(coefficients, time);
                    functions.raiseEnergy();
                }
                @Override
                public void action() {
                    MessageTemplate mt;
                    double priceForKWT = 0;
                    mt = MessageTemplate.or(MessageTemplate.and(
                                    MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("topic")),
                            MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                                    MessageTemplate.or(MessageTemplate.MatchProtocol("end_of_action"), MessageTemplate.MatchProtocol("full_of_action"))));
                    ACLMessage msg = myAgent.receive(mt);
                    if (msg != null){
                        if (msg.getProtocol().equals("topic")) {
                            if (functions.returnEnergy() > 0) {
                                priceForKWT = cfg.getPrice() / functions.returnEnergy();
                            } else if (functions.returnEnergy() == 0) {
                                priceForKWT = 0;
                            }
                            CreateTopic topic = new CreateTopic(myAgent);
                            AID subscribeOnTopic = topic.returnTopicName(msg.getContent());
                            myAgent.addBehaviour(new SendToTopic(myAgent,
                                    subscribeOnTopic,
                                    functions.returnEnergy(),
                                    priceForKWT,
                                    11, "propose_price_sell"));
                            myAgent.addBehaviour(new GenerationFSM(functions,
                                    subscribeOnTopic,
                                    priceForKWT,
                                    11,
                                    "propose_price_sell", functions.returnEnergy()));
                        }
                    }
                }
            };
            }
    }
