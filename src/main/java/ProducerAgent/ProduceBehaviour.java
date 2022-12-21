package ProducerAgent;

import ClassXML.CFGGeneration;
import ClassXML.ParametersOfGeneration;
import DF.DfHelper;
import Topic.SendToTopic;
import additionPacakge.CheckHour;
import additionPacakge.CreateTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProduceBehaviour extends Behaviour {
    private final CFGGeneration cfgGeneration;
    private CheckHour time;
    private final String START_GENERATING = "start_generating", RECEIVE_MSG = "receive_msg", REDUCE_ENERGY="reduce_energy";
    private double energy;
    private double reduce = 0;
    private Functions functions;
    private List<Double> coefficients = new ArrayList<>();
    private double fullPower = 0;
    private int myPrice;
    private MessageTemplate mt;
    private AID subscribeOnTopic;
    public ProduceBehaviour(Agent myAgent, CFGGeneration cfg, CheckHour time){
        this.myAgent = myAgent;
        this.cfgGeneration = cfg;
        this.time = time;
        myPrice = cfg.getPrice();
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Seller");
        for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
            coefficients.add(parameters.getCoef());
        }
        functions = new Functions(coefficients, time);
    }

    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("topic")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("second")));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getProtocol().equals("topic")){
                functions.raiseEnergy();
                CreateTopic topic = new CreateTopic(myAgent);
                this.subscribeOnTopic = topic.returnTopicName(msg.getContent());
//            log.debug("{} received msg with topic name {}", myAgent.getLocalName(),msg.getContent());
                myAgent.addBehaviour(new SendToTopic(myAgent,
                        this.subscribeOnTopic,
                        functions.returnEnergy(),
                        cfgGeneration.getPrice()*functions.returnEnergy(),
                        11, "propose_price_sell"));
                myAgent.addBehaviour(new GenerationFSM(functions,
                        this.subscribeOnTopic,
                        cfgGeneration.getPrice()*functions.returnEnergy(),
                        11,
                        "propose_price_sell", functions.returnEnergy()));
            } else if (msg.getProtocol().equals("second")){
//            log.debug("{} received request again", myAgent.getLocalName());
                myAgent.addBehaviour(new SendToTopic(myAgent,
                        this.subscribeOnTopic,
                        functions.returnEnergy(),
                        cfgGeneration.getPrice()*functions.returnEnergy(),
                        11, "propose_price_sell"));
                myAgent.addBehaviour(new GenerationFSM(functions,
                        this.subscribeOnTopic,
                        cfgGeneration.getPrice()*functions.returnEnergy(),
                        11,
                        "propose_price_sell", functions.returnEnergy()));
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
