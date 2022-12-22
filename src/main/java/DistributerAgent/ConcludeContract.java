package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConcludeContract extends OneShotBehaviour {
    private BidsAnalyzer bidsAnalyzer;
    private double energyRequest;
    private AID consumer;
    private double price;
    private int onEnd;
    private List <AID> sellers;
    public ConcludeContract(BidsAnalyzer bidsAnalyzer, double energyRequest, AID consumer, double price) {
        this.bidsAnalyzer = bidsAnalyzer;
        this.energyRequest = energyRequest;
        this.consumer = consumer;
        this.price = price;
    }
    @SneakyThrows
    @Override
    public void action() {
        sellers = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
//        sellers.addAll(DfHelper.findAgents(myAgent, "Seller"));
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
            log.info("{} says that {} win auction and ask to reduce it's energy for:{}", myAgent.getLocalName(),bidsAnalyzer.getBestSeller().get().getLocalName(), m.getContent().split(";")[0]);
            myAgent.send(m);

            sellers.remove(bidsAnalyzer.getBestSeller().get());

            ACLMessage zeroReduce = new ACLMessage(ACLMessage.CFP);
//            zeroReduce.addReceiver(bidsAnalyzer.getBestSeller().get());
            sellers.forEach(zeroReduce::addReceiver);
            zeroReduce.setContent(0+";"+0);
            zeroReduce.setProtocol("cfp");
            myAgent.send(zeroReduce);

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

            bidsAnalyzer.resetBest();


            this.onEnd = 1;
        }
    }

    @Override
    public int onEnd() {
//        log.debug("{} send that auction ended", getBehaviourName());
        List<AID> sellersToEnd = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
        ACLMessage real_end = new ACLMessage(ACLMessage.CONFIRM);
        sellersToEnd.forEach(real_end::addReceiver);
        real_end.setContent("");
        real_end.setProtocol("end_of_action");
        myAgent.send(real_end);
        return onEnd;
    }
}
