package DistributerAgent;

import additionPacakge.BidsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class SendRequestAndCollectBids extends ParallelBehaviour {
    private final BidsAnalyzer bidsAnalyzer;
    private double energyRequest;
    private double price;
    private AID topic;
    private List <AID> seller;
    public SendRequestAndCollectBids(BidsAnalyzer bidsAnalyzer, double energyRequest, double price, AID topic, List <AID> seller) {
        super(ParallelBehaviour.WHEN_ANY);
        this.bidsAnalyzer = bidsAnalyzer;
        this.energyRequest = energyRequest;
        this.price = price;
        this.topic = topic;
        this.seller = seller;
    }
    @Override
    public void onStart() {
        addSubBehaviour(new CollectBids(bidsAnalyzer, energyRequest, price, topic));
        addSubBehaviour(new WakerBehaviour(myAgent, 2500) {
            @Override
            protected void onWake() {
            }
        });
    }

    @Override
    public int onEnd() {
        if (bidsAnalyzer.getBestSeller().isPresent()){
            return 1;
        } else if (bidsAnalyzer.getBestSeller().isEmpty()){
            log.debug("{} didn't found best seller", myAgent.getLocalName());
            ACLMessage m = new ACLMessage(ACLMessage.CANCEL);
            seller.forEach(m::addReceiver);
            m.setProtocol("refuse");
            m.setContent("refuse");
            myAgent.send(m);
            log.debug("{} send refuse to seller", myAgent.getLocalName());
            return 2;
        } else return 2;
//                bidsAnalyzer.getBestSeller().isPresent() ? 1 : 2;
    }
}
