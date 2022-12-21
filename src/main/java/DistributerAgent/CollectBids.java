package DistributerAgent;

import DF.DfHelper;
import Topic.SendToTopic;
import additionPacakge.BidsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class CollectBids extends Behaviour {
    private int requestsCount;
    private final List<ACLMessage> responses = new ArrayList<>();

    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
            MessageTemplate.MatchProtocol("propose_price_sell"));
    private final BidsAnalyzer bidsAnalyzer;
    private double energyRequest;
    private double price;
    private AID topic;
    public CollectBids(BidsAnalyzer bidsAnalyzer, double energyRequest, double price, AID topic) {
        this.bidsAnalyzer = bidsAnalyzer;
        this.energyRequest = energyRequest;
        this.price = price;
        this.topic = topic;
    }
    @SneakyThrows
    @Override
    public void onStart() {
//        myAgent.addBehaviour(new SendToTopic(myAgent, topic, energyRequest, price, 11, "propose_price_buy"));
        List<AID> sellers = DfHelper.findAgents(myAgent, "Seller");
        requestsCount = sellers.size();
    }

    @Override
    public void action() {
        ACLMessage response = myAgent.receive(mt);
        if (response != null){
//            log.debug("From {} topic received propose with this amount of energy {} and this price {}",
//            response.getContent().split(";")[0],
//            response.getContent().split(";")[1],
//            response.getContent().split(";")[2]);
            responses.add(response);
            bidsAnalyzer.putBid(response, energyRequest);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return responses.size() == requestsCount;
    }
}
