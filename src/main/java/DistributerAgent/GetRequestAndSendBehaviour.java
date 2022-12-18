package DistributerAgent;

import DF.DfHelper;
import additionPacakge.CreateTopic;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetRequestAndSendBehaviour extends Behaviour {
    private MessageTemplate mt;

    private List <AID> seller;
    public GetRequestAndSendBehaviour(Agent myAgent){
        this.myAgent = myAgent;
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Distributor");
    }

    @Override
    public void action() {
        seller = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
        mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("request"));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){
//            log.debug("{} needs this amount of energy {} at {} hours peak price {}",
//                    msg.getSender().getLocalName(),
//                    msg.getContent().split(";")[0],
//                    msg.getContent().split(";")[1],
//                    msg.getContent().split(";")[2]);
            CreateTopic topic = new CreateTopic(myAgent);
            ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
            seller.forEach(request::addReceiver);
            request.setProtocol("buying");
            request.setContent(topic.returnTopicName(myAgent.getLocalName()).getName()+";"+msg.getContent().split(";")[0]+";"+msg.getContent().split(";")[2]);
            myAgent.send(request);
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
