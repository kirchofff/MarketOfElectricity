package ProducerAgent;

import Topic.SendToTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReceiveDividedResult extends Behaviour {
    private Functions functions;
    private int result;
    private boolean done = false;
    private MessageTemplate mt;
    private AID topic;
    private double amountOfEnergy;
    private double price;
    private int performative;
    private String protocol;
    private Agent agent;
    public ReceiveDividedResult (Functions functions, Agent agent, AID topic, double amountOfEnergy, double price, int performative, String protocol){
        this.functions = functions;
        this.topic = topic;
        this.amountOfEnergy = amountOfEnergy;
        this.price = price;
        this.performative = performative;
        this.protocol = protocol;
        this.agent = agent;
    }

    @Override
    public void onStart() {
//        log.debug("{} has started", getBehaviourName());
        myAgent.addBehaviour(new SendToTopic(myAgent, topic, amountOfEnergy, price, performative, protocol));
    }

    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchProtocol("cfp")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CANCEL), MessageTemplate.MatchProtocol("refuse")));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getPerformative() == 3 && msg.getProtocol().equals("cfp")){
                functions.reduceEnergy(Double.parseDouble(msg.getContent().split(";")[0]));
                log.debug("{} reduce energy for {} and get {}", myAgent.getLocalName(), msg.getContent().split(";")[0], msg.getContent().split(";")[1]);
                result = 1;
                done = true;
            } else if (msg.getPerformative() == 2 && msg.getProtocol().equals("refuse")){
                result = 0;
                done = true;
            }
//            log.debug("{} received msg with topic name {}", myAgent.getLocalName(),msg.getContent());

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
