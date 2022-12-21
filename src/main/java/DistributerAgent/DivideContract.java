package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import additionPacakge.DivideBitsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class DivideContract extends ParallelBehaviour {
    private final DivideBitsAnalyzer bidsAnalyzer;
    private double energyRequest;
    private double price;
    private AID topic;
    private List <AID> seller;

    public DivideContract(DivideBitsAnalyzer bidsAnalyzer, double energyRequest, double price, AID topic, List <AID> seller) {
        super(ParallelBehaviour.WHEN_ANY);
        this.bidsAnalyzer = bidsAnalyzer;
        this.energyRequest = energyRequest;
        this.price = price;
        this.topic = topic;
        this.seller = seller;
    }
    @Override
    public void onStart() {
        log.info("Distributor try to divide contract");
        addSubBehaviour(new CollectDivideBids(bidsAnalyzer, energyRequest, price, topic));
        addSubBehaviour(new WakerBehaviour(myAgent, 3000) {
            @Override
            protected void onWake() {
            }
        });
    }

    @Override
    public int onEnd() {
        if (bidsAnalyzer.isSuccess()){
            return 3;
        } else if (!bidsAnalyzer.isSuccess()){
            log.debug("{} cant even divide", myAgent.getLocalName());
            myAgent.addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    ACLMessage m = new ACLMessage(ACLMessage.CANCEL);
                    seller.forEach(m::addReceiver);
                    m.setProtocol("refuse");
                    myAgent.send(m);
                    log.debug("{} send refuse to seller", myAgent.getLocalName());
                    myAgent.removeBehaviour(this);
                }
            });
            return 4;
        } else return 4;
    }
}
