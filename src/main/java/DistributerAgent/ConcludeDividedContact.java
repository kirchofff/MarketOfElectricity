package DistributerAgent;

import additionPacakge.BidsAnalyzer;
import additionPacakge.DivideBitsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConcludeDividedContact extends OneShotBehaviour {
    private DivideBitsAnalyzer bidsAnalyzer;
    private AID consumer;
    private double energyRequest;
    public ConcludeDividedContact(DivideBitsAnalyzer bidsAnalyzer, AID consumer, double energyRequest) {
        this.bidsAnalyzer = bidsAnalyzer;
        this.consumer = consumer;
        this.energyRequest = energyRequest;
    }

    @Override
    public void action() {
        List <AID> sellers = bidsAnalyzer.getSellers();
        double sendEnergyToConsumer = energyRequest;
        double divideEnergy = 0;
        double maxEnergy = 0;
        double fullPrice = 0;
        AID maxSeller = new AID();
        double maxPrice = 0;
        for (int i = 0; i < bidsAnalyzer.getSellers().size(); i++){
            divideEnergy += bidsAnalyzer.getEnergyOfSenders().get(i);
            if (bidsAnalyzer.getEnergyOfSenders().get(i) > maxEnergy){
                maxEnergy = bidsAnalyzer.getEnergyOfSenders().get(i);
                maxSeller = bidsAnalyzer.getSellers().get(i);
                maxPrice = bidsAnalyzer.getFullPrice().get(i);
            }
        }

        fullPrice += maxPrice;
        energyRequest -= maxEnergy;

        log.debug("full energy is {}", divideEnergy);
        log.debug("max energy is {}", maxEnergy);
        log.debug("max seller is {}", maxSeller.getLocalName());

        ACLMessage maxMSG = new ACLMessage(ACLMessage.CFP);
        maxMSG.addReceiver(maxSeller);
        maxMSG.setContent(maxEnergy+";"+maxPrice);
        maxMSG.setProtocol("cfp");
        myAgent.send(maxMSG);
        log.debug("Distributor say to reduce energy {} to {} and send him {} $", maxEnergy, maxSeller.getLocalName(), maxPrice);
        log.debug("Energy is needed {}", energyRequest);
        sellers.remove(maxSeller);


        for (int i = 0; i < sellers.size(); i++){
            if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) > 0){
                ACLMessage m = new ACLMessage(ACLMessage.CFP);
                m.addReceiver(bidsAnalyzer.getSellers().get(i));
                m.setContent(bidsAnalyzer.getEnergyOfSenders().get(i)+";"+bidsAnalyzer.getFullPrice().get(i));
                m.setProtocol("cfp");
                myAgent.send(m);
                fullPrice = bidsAnalyzer.getFullPrice().get(i);
                energyRequest -= bidsAnalyzer.getEnergyOfSenders().get(i);
                log.debug("{} send divided reduce energy {}", getBehaviourName(), bidsAnalyzer.getEnergyOfSenders().get(i));
            } else if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) < 0){
                ACLMessage m = new ACLMessage(ACLMessage.CFP);
                m.addReceiver(bidsAnalyzer.getSellers().get(i));
                double priceForMWT = (bidsAnalyzer.getFullPrice().get(i)/bidsAnalyzer.getEnergyOfSenders().get(i))*energyRequest;
                m.setContent(energyRequest+";"+priceForMWT);
                m.setProtocol("cfp");
                myAgent.send(m);
                fullPrice = priceForMWT;
                energyRequest -= energyRequest;
                log.debug("{} send divided reduce energy {}", getBehaviourName(), bidsAnalyzer.getEnergyOfSenders().get(i));
            }
        }
        double finalFullPrice = fullPrice;
        myAgent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage mc = new ACLMessage(ACLMessage.CFP);
                mc.addReceiver(consumer);
                mc.setProtocol("deal");
                mc.setContent(sendEnergyToConsumer+";"+ finalFullPrice);
                myAgent.send(mc);
            }
        });
        myAgent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                bidsAnalyzer.resetDivideContract();
            }
        });
    }

    @Override
    public int onEnd() {
        return 1;
    }
}
