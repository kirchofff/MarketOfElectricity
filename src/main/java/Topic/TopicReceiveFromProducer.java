package Topic;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicReceiveFromProducer extends Behaviour {
    private MessageTemplate mt;

    @Override
    public void onStart() {
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchProtocol("OnlyMyTopic")  );
    }

    @Override
    public void action() {
        ACLMessage receive = getAgent().receive(mt);
        if (receive != null){
            log.info(receive.getContent());
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
