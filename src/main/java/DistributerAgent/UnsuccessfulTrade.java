package DistributerAgent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

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
        return 1;
    }
}
