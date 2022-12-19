package ProducerAgent;

import DF.DfHelper;
import Topic.SendToTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecieveMsgWithTopicEnergyPrice extends Behaviour {
    private MessageTemplate mt;
    private boolean done = false;
    private int priceForMWT;
    private Functions functions;
    public RecieveMsgWithTopicEnergyPrice (double energy, int priceForMWT, Functions functions){
        this.priceForMWT = priceForMWT;
        this.functions = functions;
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
            functions.raiseEnergy();
            AID topic = new AID(msg.getContent().split(";")[0]);
            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
            request.setProtocol("OnlyMyTopic");
            request.addReceiver(topic);
            request.setContent(msg.getContent().split(";")[1]+";"+msg.getContent().split(";")[2]+";"+functions.returnEnergy()+";"+priceForMWT*functions.returnEnergy());
            myAgent.send(request);
//            myAgent.addBehaviour(new SendToTopic(
//                    myAgent,
//                    topic,
//                    Double.parseDouble(msg.getContent().split(";")[1]),
//                    Integer.parseInt(msg.getContent().split(";")[2]))
//                    );
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
