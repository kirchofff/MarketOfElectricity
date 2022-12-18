package ProducerAgent;

import DF.DfHelper;
import Topic.SendToTopic;
import Topic.TopicReceive;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecieveMsgWithTopicEnergyPrice extends Behaviour {
    private MessageTemplate mt;
    private AID topic;
    public RecieveMsgWithTopicEnergyPrice (){
        this.topic = topic;
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Seller");
    }

    @Override
    public void action() {
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchProtocol("buying"));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            AID topic = new AID(msg.getContent().split(";")[0]);
//            topic.setLocalName(msg.getContent().split(";")[0]);
//            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
//            request.setProtocol("OnlyMyTopic");
//            request.addReceiver(topic);
//            log.info("{} send to topic request from distributor for this amount of energy {} with price {}",
//                    myAgent.getLocalName(),
//                    msg.getContent().split(";")[1],
//                    msg.getContent().split(";")[2]);
//            myAgent.addBehaviour(new TopicReceive());
//            request.setContent(msg.getContent().split(";")[1]+msg.getContent().split(";")[2]);
//            myAgent.send(request);
            myAgent.addBehaviour(new SendToTopic(myAgent, topic, Double.parseDouble(msg.getContent().split(";")[1]), Integer.parseInt(msg.getContent().split(";")[2])));
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
