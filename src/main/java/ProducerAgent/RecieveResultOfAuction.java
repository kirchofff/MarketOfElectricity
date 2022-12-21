package ProducerAgent;

import Topic.SendToTopic;
import additionPacakge.CreateTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecieveResultOfAuction extends Behaviour {
    private MessageTemplate mt;
    private Functions functions;
    private int result;
    private boolean done = false;
    public RecieveResultOfAuction (Functions functions){
        this.functions = functions;
    }

    @Override
    public void onStart() {
//        log.debug("receive result generation has started");
    }

    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchProtocol("cfp")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CANCEL), MessageTemplate.MatchProtocol("refuse")));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getPerformative() == 3 && msg.getProtocol().equals("cfp")){
                functions.reduceEnergy(Double.parseDouble(msg.getContent().split(";")[0]));
//                log.debug("{} reduce energy for {} and get {}", myAgent.getLocalName(), msg.getContent().split(";")[0], msg.getContent().split(";")[1]);
                result = 1;
                done = true;
            } else if (msg.getPerformative() == 2 && msg.getProtocol().equals("refuse")){
//                log.debug("receive refuse", getBehaviourName());
                result = 0;
                done = true;
            }
//            log.debug("{} received msg with topic name {}", myAgent.getLocalName(),msg.getContent());

        } else {
            block();
        }
    }

    @Override
    public int onEnd() {
        return result;
    }

    @Override
    public boolean done() {
        return done;
    }
}
