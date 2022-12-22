package DistributerAgent;

import DF.DfHelper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UnsuccessfulTrade extends OneShotBehaviour {
    private AID consumer;
    public UnsuccessfulTrade (AID consumer){
        this.consumer =consumer;
    }
    @Override
    public void action() {
        ACLMessage m = new ACLMessage(ACLMessage.CANCEL);
        m.addReceiver(consumer);
        m.setContent("Unsuccessful trade");
        m.setProtocol("fail_trade");
        myAgent.send(m);
        log.debug("unsuccessful trade");
    }

    @Override
    public int onEnd() {
        log.debug("{} send that auction ended", getBehaviourName());
        List<AID> sellersToEnd  = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
        ACLMessage real_end = new ACLMessage(ACLMessage.CONFIRM);
        sellersToEnd.forEach(real_end::addReceiver);
        real_end.setContent("");
        real_end.setProtocol("end_of_action");
        myAgent.send(real_end);
        return 1;
    }
}
