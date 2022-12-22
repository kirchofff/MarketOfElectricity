package ProducerAgent;

import ClassXML.CFGGeneration;
import ClassXML.ParametersOfGeneration;
import ConsumerAgent.GetRequest;
import DF.DfHelper;
import Topic.SendToTopic;
import additionPacakge.CheckHour;
import additionPacakge.CreateTopic;
import additionPacakge.Functions;
import additionPacakge.QueueDecider;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

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
    private int currentTime;
    private List <AID> queue;
    private QueueDecider queueDecider;
//    private Map<AID, Integer> queue;

    public ProduceBehaviour(Agent myAgent, CFGGeneration cfg, CheckHour time, List <AID> queue, QueueDecider queueDecider){
        this.myAgent = myAgent;
        this.cfgGeneration = cfg;
        this.time = time;
        myPrice = cfg.getPrice();
//        this.decider = decider;
        this.queue = queue;
        this.queueDecider = queueDecider;
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Seller");
        for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
            coefficients.add(parameters.getCoef());
        }
        functions = new Functions(coefficients, time);
        functions.raiseEnergy();
        currentTime = time.returnCurrentTime();
    }
    @SneakyThrows
    @Override
    @Synchronized
    public void action() {
        double priceForKWT = 0;
        mt = MessageTemplate.or(MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("topic")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                        MessageTemplate.or(MessageTemplate.MatchProtocol("end_of_action"), MessageTemplate.MatchProtocol("full_of_action"))));
        ACLMessage msg = myAgent.receive(mt);
        if (currentTime != time.returnCurrentTime()){
            functions.raiseEnergy();
            currentTime = time.returnCurrentTime();
        }
        if (msg != null) {
            if (!queue.contains(msg.getSender())){
                queue.add(msg.getSender());
                queueDecider.addSender(msg.getSender());
//                log.debug("{} added to queue {}", queueDecider.getSenders());
//                Thread.sleep(200);
            }
//            if (queue.size() == 1){
            if (queueDecider.getFirst().equals(msg.getSender())){
//                log.info("Permission to bid sent to {}", queueDecider.getFirst().getLocalName());
                ACLMessage m = new ACLMessage(ACLMessage.DISCONFIRM);
                m.addReceiver(queueDecider.getFirst());
                m.setContent("");
                m.setProtocol("go");
                myAgent.send(m);
                if (msg.getProtocol().equals("topic")) {
                    if (functions.returnEnergy() > 0) {
                        priceForKWT = cfgGeneration.getPrice() / functions.returnEnergy();
                    } else if (functions.returnEnergy() == 0) {
                        priceForKWT = 0;
                    }
                    CreateTopic topic = new CreateTopic(myAgent);
                    this.subscribeOnTopic = topic.returnTopicName(msg.getContent());
//            log.debug("{} received msg with topic name {}", myAgent.getLocalName(),msg.getContent());
                    myAgent.addBehaviour(new SendToTopic(myAgent,
                            this.subscribeOnTopic,
                            functions.returnEnergy(),
                            priceForKWT,
                            11, "propose_price_sell"));
                    myAgent.addBehaviour(new GenerationFSM(functions,
                            this.subscribeOnTopic,
                            priceForKWT,
                            11,
                            "propose_price_sell", functions.returnEnergy()));
                }
            }else if (msg.getProtocol().equals("topic") && queue.size() > 1) {
                ACLMessage m = new ACLMessage(ACLMessage.DISCONFIRM);
                for (int i = 1; i < queueDecider.getSenders().size(); i++){
                    m.addReceiver(queueDecider.getSenders().get(i));
                    log.debug("{} ASK TO SLEEP {}",myAgent.getLocalName(),queue.get(i).getLocalName());
                }
                m.setContent("confirm");
                m.setProtocol("busy");
                myAgent.send(m);
                queueDecider.removeSender(msg.getSender());
        }
            if (msg.getProtocol().equals("end_of_action")){
//                queue.remove(msg.getSender());
//                log.debug("{} ended auction removed from queue", msg.getSender().getLocalName());
                //                log.debug("{} ended auction now in queue: ", queue.toString());
                queue.clear();
                queueDecider.clearSenders();
            }
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
