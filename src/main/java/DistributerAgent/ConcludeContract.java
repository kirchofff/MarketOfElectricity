package DistributerAgent;

import additionPacakge.BidsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcludeContract extends OneShotBehaviour {
    private BidsAnalyzer bidsAnalyzer;
    private double energyRequest;
    private AID consumer;
    private double price;
    private int onEnd;
    public ConcludeContract(BidsAnalyzer bidsAnalyzer, double energyRequest, AID consumer, double price) {
        this.bidsAnalyzer = bidsAnalyzer;
        this.energyRequest = energyRequest;
        this.consumer = consumer;
        this.price = price;
    }
    @SneakyThrows
    @Override
    public void action() {
        if (bidsAnalyzer.getBestValue() > price){
            myAgent.addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    ACLMessage m = new ACLMessage(ACLMessage.CFP);
                    m.addReceiver(consumer);
                    m.setProtocol("deal");
                    m.setContent(energyRequest+";"+bidsAnalyzer.getBestValue());
                    myAgent.send(m);
                }
            });
            this.onEnd = 2;
        } else if (bidsAnalyzer.getBestValue() < price){
            ACLMessage m = new ACLMessage(ACLMessage.CFP);
            m.addReceiver(bidsAnalyzer.getBestSeller().get());
            m.setContent(energyRequest+";"+bidsAnalyzer.getBestValue());
            m.setProtocol("cfp");
            log.debug("{} says to reduce this energy :{}",myAgent.getLocalName(), m.getContent().split(";")[0]);
            myAgent.send(m);
            myAgent.addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    ACLMessage m = new ACLMessage(ACLMessage.CFP);
                    m.addReceiver(consumer);
                    m.setProtocol("deal");
                    m.setContent(energyRequest+";"+bidsAnalyzer.getBestValue());
                    myAgent.send(m);
                }
            });
            myAgent.addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    bidsAnalyzer.resetBest();
                }
            });
            this.onEnd = 1;
        }
    }

    @Override
    public int onEnd() {
        return onEnd;
    }
}
