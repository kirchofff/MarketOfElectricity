package Trash;

import Topic.SendToTopic;
import additionPacakge.CreateTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecieveMsgWithTopic extends Behaviour {
    private MessageTemplate mt;
    private boolean done = false;
    private int priceForMWT;
    private Functions functions;
    public RecieveMsgWithTopic(double energy, int priceForMWT, Functions functions){
        this.priceForMWT = priceForMWT;
        this.functions = functions;
    }

    @Override
    public void onStart() {
//        DfHelper.registerAgent(myAgent, "Seller");
    }

    @Override
    public void action() {
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("topic"));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            functions.raiseEnergy();
            CreateTopic topic = new CreateTopic(myAgent);
            AID subscribeOnTopic = topic.returnTopicName(msg.getContent());
//            log.debug("{} received msg with topic name {}", myAgent.getLocalName(),msg.getContent());
            myAgent.addBehaviour(new SendToTopic(myAgent, subscribeOnTopic, functions.returnEnergy(), priceForMWT, 11, "propose_price_sell"));
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public int onEnd() {
        return 1;
    }

}
