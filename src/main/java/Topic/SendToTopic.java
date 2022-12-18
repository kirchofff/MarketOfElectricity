package Topic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendToTopic extends Behaviour {
    private  AID topic;
    private double amountOfEnergy;
    private int price;
    private boolean done = false;
    public SendToTopic(Agent myAgent, AID topic, double amountOfEnergy, int price){
        this.myAgent = myAgent;
        this.topic = topic;
        this.amountOfEnergy = amountOfEnergy;
        this.price = price;
    }

    @Override
    public void onStart() {
        ACLMessage request = new ACLMessage(ACLMessage.INFORM);
        request.setProtocol("OnlyMyTopic");
        request.addReceiver(topic);
        log.info("{} send to topic request from distributor for this amount of energy {} with price {}",
                myAgent.getLocalName(),
                amountOfEnergy,
                price);
        request.setContent(amountOfEnergy+";"+price);
        myAgent.send(request);
        myAgent.removeBehaviour(this);
    }

    @Override
    public void action() {
    }


    @Override
    public boolean done() {
        return false;
    }
}
