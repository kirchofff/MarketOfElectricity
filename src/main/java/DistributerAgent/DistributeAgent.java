package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import additionPacakge.CreateTopic;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DistributeAgent extends Behaviour {
    private MessageTemplate mt;
    private List<AID> seller;
    public DistributeAgent(Agent myAgent){
        this.myAgent = myAgent;
    }
    @SneakyThrows
    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, myAgent.getLocalName());
        Thread.sleep(300);
        this.seller = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
    }

    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("request")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("request_second")));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){
            if (msg.getProtocol().equals("request")){
                log.debug("{} needs this amount of energy {} at {} hours peak price {}",
                        msg.getSender().getLocalName(),
                        msg.getContent().split(";")[0],
                        msg.getContent().split(";")[1],
                        msg.getContent().split(";")[2]);
                CreateTopic topic = new CreateTopic(myAgent);
                AID subscribeOnTopic = topic.returnTopicName(myAgent.getLocalName());
                ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
                seller.forEach(request::addReceiver);
                request.setProtocol("topic");
                request.setContent(subscribeOnTopic.getLocalName());
                myAgent.send(request);
                myAgent.addBehaviour(new DistributeFSM (
                        myAgent,
                        Double.parseDouble(msg.getContent().split(";")[0]),
                        Double.parseDouble(msg.getContent().split(";")[2]),
                        subscribeOnTopic,
                        msg.getSender(),
                        seller));
            } else if (msg.getProtocol().equals("request_second")){
                log.debug("{} needs this amount of energy {} at {} hours peak price {}",
                        msg.getSender().getLocalName(),
                        msg.getContent().split(";")[0],
                        msg.getContent().split(";")[1],
                        msg.getContent().split(";")[2]);
                CreateTopic topic = new CreateTopic(myAgent);
                AID subscribeOnTopic = topic.returnTopicName(myAgent.getLocalName());
                ACLMessage request = new ACLMessage(ACLMessage.SUBSCRIBE);
                seller.forEach(request::addReceiver);
                request.setProtocol("second");
                request.setContent(subscribeOnTopic.getLocalName());
                myAgent.send(request);
                myAgent.addBehaviour(new DistributeFSM (
                        myAgent,
                        Double.parseDouble(msg.getContent().split(";")[0]),
                        Double.parseDouble(msg.getContent().split(";")[2]),
                        subscribeOnTopic,
                        msg.getSender(),
                        seller));
            }
        }else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }





}
