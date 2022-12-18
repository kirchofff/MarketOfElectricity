package DistributerAgent;

import additionPacakge.BidsAnalyzer;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class SendRequestsAndCollectBids extends ParallelBehaviour {

    private BidsAnalyzer bidsAnalyzer;
    public SendRequestsAndCollectBids(BidsAnalyzer bidsAnalyzer) {
        super(ParallelBehaviour.WHEN_ANY);
        this.bidsAnalyzer = bidsAnalyzer;
    }

    @Override
    public void onStart() {
        addSubBehaviour(new CollectBids(bidsAnalyzer));
        addSubBehaviour(new WakerBehaviour(myAgent, 1000) {
            @Override
            protected void onWake() {
            }
        });
    }

    @Override
    public int onEnd() {
        return bidsAnalyzer.getBestSeller().isPresent() ? 1 : 2;
    }
}
