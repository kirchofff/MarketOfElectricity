package Trash;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicReceive extends Behaviour {
    private MessageTemplate mt;

    @Override
    public void onStart() {
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("OnlyMyTopic")  );
    }

    @Override
    public void action() {
        ACLMessage receive = getAgent().receive(mt);
        if (receive != null){
            log.info("Consumer send request for energy in amount {} with price {}, now {} have this amount of energy {} with price {}",
                    receive.getContent().split(";")[0],
                    receive.getContent().split(";")[1],
                    receive.getSender().getLocalName(),
                    receive.getContent().split(";")[2],
                    receive.getContent().split(";")[3]);
//            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
//            request.setProtocol("bet");
//            request.addReceiver(topic);
////        log.info("{} send to topic request from distributor for this amount of energy {} with price {}",
////                myAgent.getLocalName(),
////                amountOfEnergy,
////                price);
//            request.setContent(amountOfEnergy+";"+price);
//            myAgent.send(request);
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
