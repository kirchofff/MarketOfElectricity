package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import additionPacakge.CreateTopic;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DistributeAgent extends Behaviour {
    private MessageTemplate mt;
    private List<AID> seller;
    private List <AID> senders = new ArrayList<>();
    private double price;
    private double energy;
    public DistributeAgent(Agent myAgent){
        this.myAgent = myAgent;
    }
    @SneakyThrows
    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, myAgent.getLocalName());
        Thread.sleep(300);
        seller = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
    }
    @SneakyThrows
    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("request")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM), MessageTemplate.or(
                        MessageTemplate.MatchProtocol("wait"), MessageTemplate.MatchProtocol("go"))));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){
            if (msg.getProtocol().equals("request")){
                log.debug("{} needs this amount of energy {} at {} hours peak price {}",
                        msg.getSender().getLocalName(),
                        msg.getContent().split(";")[0],
                        msg.getContent().split(";")[1],
                        msg.getContent().split(";")[2]);
                CreateTopic topic = new CreateTopic(myAgent);
                AID subscribeOnTopic = topic.returnTopicName(myAgent.getLocalName());
                ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
                seller.forEach(request::addReceiver);
                request.setProtocol("topic");
                request.setContent(subscribeOnTopic.getLocalName());
                myAgent.send(request);
                price = Double.parseDouble(msg.getContent().split(";")[2]);
                energy = Double.parseDouble(msg.getContent().split(";")[0]);
                myAgent.addBehaviour(new DistributeFSM (
                        myAgent,
                        Double.parseDouble(msg.getContent().split(";")[0]),
                        Double.parseDouble(msg.getContent().split(";")[2]),
                        subscribeOnTopic,
                        msg.getSender(),
                        seller));
            }else if(msg.getProtocol().equals("go")){

            }
            else if (msg.getProtocol().equals("wait")){
                log.debug("{} ask {} to wait", msg.getSender().getLocalName(), myAgent.getLocalName());
                Thread.sleep(5000);
            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }





}
