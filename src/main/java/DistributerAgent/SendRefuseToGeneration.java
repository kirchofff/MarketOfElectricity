package DistributerAgent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class SendRefuseToGeneration extends OneShotBehaviour {
    private List<AID> seller;
    private MessageTemplate mt;
    public SendRefuseToGeneration (List <AID> seller){
        this.seller = seller;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void action() {

    }

}
