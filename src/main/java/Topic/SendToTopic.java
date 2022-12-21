package Topic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendToTopic extends OneShotBehaviour {
    private  AID topic;
    private double amountOfEnergy;
    private double priceForMWT;
    private boolean done = false;
    private int performative;
    private String protocol;
    private Agent agent;
    public SendToTopic(Agent agent, AID topic, double amountOfEnergy, double priceForMWT, int performative, String protocol){
        this.topic = topic;
        this.amountOfEnergy = amountOfEnergy;
        this.priceForMWT = priceForMWT;
        this.performative = performative;
        this.protocol = protocol;
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage request = new ACLMessage(performative);
        request.setProtocol(protocol);
        request.addReceiver(topic);
        log.info("{} send to topic request with energy {} with price {}",
                myAgent.getLocalName(),
                amountOfEnergy,
                priceForMWT);
        request.setContent(agent.getLocalName()+";"+amountOfEnergy+";"+ priceForMWT);
        myAgent.send(request);
    }

    @Override
    public int onEnd() {
        return 1;
    }
}
