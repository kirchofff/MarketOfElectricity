package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class CollectBids extends Behaviour {
    private int requestsCount;
    private final List<ACLMessage> responses = new ArrayList<>();
    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
            MessageTemplate.MatchProtocol("buying"));
    private final BidsAnalyzer bidsAnalyzer;

    public CollectBids(BidsAnalyzer bidsAnalyzer) {
        this.bidsAnalyzer = bidsAnalyzer;
    }

    @Override
    public void onStart() {
        List<AID> sellers = DfHelper.findAgents(myAgent, "Seller");
        ACLMessage req  = new ACLMessage(ACLMessage.CFP);
        req.setProtocol("buying");
        sellers.forEach(req::addReceiver);
        myAgent.send(req);
        requestsCount = sellers.size();
    }

    @Override
    public void action() {
        ACLMessage response = myAgent.receive(mt);
        if (response != null){
            responses.add(response);
            bidsAnalyzer.putBid(response);
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return responses.size() == requestsCount;
    }
}
