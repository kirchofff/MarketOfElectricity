package DistributerAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistributeAgent extends FSMBehaviour {
    private static final String GET_REQUEST = "send_request",COLLECT_BIDS="collect_bids", DEAL_WITH_WINNER="deal_with_winner", END="end";
    public DistributeAgent(Agent myAgent){
        this.myAgent = myAgent;
    }
    @SneakyThrows
    @Override
    public void onStart() {
//        BidsAnalyzer bidsAnalyzer = new BidsAnalyzer();
        this.registerFirstState(new GetRequestAndSendBehaviour(myAgent), GET_REQUEST);

//        this.registerFirstState(new SendRequestsAndCollectBids(bidsAnalyzer), COLLECT_BIDS);
    }



}
