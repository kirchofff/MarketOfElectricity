package DistributerAgent;

import DF.DfHelper;
import additionPacakge.BidsAnalyzer;
import additionPacakge.DivideBitsAnalyzer;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConcludeDividedContact extends OneShotBehaviour {
    private DivideBitsAnalyzer bidsAnalyzer;
    private AID consumer;
    private double energyRequest;
    private double priceFromConsumer;
    private int onEnd;
    private List <AID> sellersToRefuse = new ArrayList<>();
    public ConcludeDividedContact(DivideBitsAnalyzer bidsAnalyzer, AID consumer, double energyRequest, double priceFromConsumer) {
        this.bidsAnalyzer = bidsAnalyzer;
        this.consumer = consumer;
        this.energyRequest = energyRequest;
        this.priceFromConsumer = priceFromConsumer;
    }

    @Override
    public void action() {
        sellersToRefuse.addAll(DfHelper.findAgents(myAgent, "Seller"));
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
        sellers.remove(maxSeller);
        fullPrice += maxPrice;
        energyRequest -= maxEnergy;
//        log.debug("full energy is {}", divideEnergy);
//        log.debug("max energy is {}", maxEnergy);
//        log.debug("max seller is {}", maxSeller.getLocalName());

        for (int i = 0; i < sellers.size(); i++){
            if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) > 0){
                fullPrice += bidsAnalyzer.getFullPrice().get(i);
            } else if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) < 0){
                double priceForMWT = (bidsAnalyzer.getFullPrice().get(i)/bidsAnalyzer.getEnergyOfSenders().get(i))*energyRequest;
                fullPrice += priceForMWT;
            }
        }
        if (fullPrice < priceFromConsumer){
            sellersToRefuse.remove(maxSeller);
            ACLMessage maxMSG = new ACLMessage(ACLMessage.CFP);
            maxMSG.addReceiver(maxSeller);
            maxMSG.setContent(maxEnergy+";"+maxPrice);
            maxMSG.setProtocol("cfp");
            myAgent.send(maxMSG);
            log.info("Distributor say to reduce energy {} for {} and send him {} $", maxEnergy, maxSeller.getLocalName(), maxPrice);
//            log.debug("Energy needed {}", energyRequest);
            for (int i = 0; i < sellers.size(); i++){
                if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) > 0){
                    ACLMessage m = new ACLMessage(ACLMessage.CFP);
                    m.addReceiver(bidsAnalyzer.getSellers().get(i));
                    m.setContent(bidsAnalyzer.getEnergyOfSenders().get(i)+";"+bidsAnalyzer.getFullPrice().get(i));
                    m.setProtocol("cfp");
                    myAgent.send(m);
                    energyRequest -= bidsAnalyzer.getEnergyOfSenders().get(i);
                    log.info("Distributor say to reduce energy {} for {} and send him {} $"
                            , bidsAnalyzer.getSellers().get(i).getLocalName(),
                            bidsAnalyzer.getEnergyOfSenders().get(i)
                            ,bidsAnalyzer.getFullPrice().get(i));
                    sellersToRefuse.remove(bidsAnalyzer.getSellers().get(i));
                } else if (energyRequest - bidsAnalyzer.getEnergyOfSenders().get(i) < 0){
                    ACLMessage m = new ACLMessage(ACLMessage.CFP);
                    m.addReceiver(bidsAnalyzer.getSellers().get(i));
                    double priceForMWT = (bidsAnalyzer.getFullPrice().get(i)/bidsAnalyzer.getEnergyOfSenders().get(i))*energyRequest;
                    m.setContent(energyRequest+";"+priceForMWT);
                    m.setProtocol("cfp");
                    myAgent.send(m);
                    log.info("Distributor say last time to reduce energy {} for {} and send him {} $"
                            , bidsAnalyzer.getSellers().get(i).getLocalName()
                            , energyRequest
                            ,priceForMWT);
                    energyRequest -= energyRequest;
                    sellersToRefuse.remove(bidsAnalyzer.getSellers().get(i));

                }
            }
            ACLMessage mc = new ACLMessage(ACLMessage.CANCEL);
            sellersToRefuse.forEach(mc::addReceiver);
            mc.setProtocol("refuse");
            mc.setContent("");
            myAgent.send(mc);

            ACLMessage messageForConsumer = new ACLMessage(ACLMessage.CFP);
            messageForConsumer.addReceiver(consumer);
            messageForConsumer.setProtocol("deal");
            messageForConsumer.setContent(sendEnergyToConsumer+";"+ fullPrice);
            myAgent.send(messageForConsumer);

            bidsAnalyzer.resetDivideContract();
            onEnd = 1;
        } else if (fullPrice > priceFromConsumer){
            log.info("Price is too high");
            double finalFullPrice1 = fullPrice;
            ACLMessage mc = new ACLMessage(ACLMessage.CANCEL);
            sellersToRefuse.forEach(mc::addReceiver);
            mc.setProtocol("refuse");
            mc.setContent("");
            myAgent.send(mc);
            myAgent.addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    ACLMessage mc = new ACLMessage(ACLMessage.CFP);
                    mc.addReceiver(consumer);
                    mc.setProtocol("deal");
                    mc.setContent(sendEnergyToConsumer+";"+ finalFullPrice1);
                    myAgent.send(mc);
                }
            });
            bidsAnalyzer.resetDivideContract();
            onEnd = 2;
        }
    }

    @Override
    public int onEnd() {
            log.debug("{} send that auction ended", getBehaviourName());
            List <AID> sellersToEnd  = new ArrayList<>(DfHelper.findAgents(myAgent, "Seller"));
            ACLMessage real_end = new ACLMessage(ACLMessage.CONFIRM);
            sellersToEnd.forEach(real_end::addReceiver);
            real_end.setContent("");
            real_end.setProtocol("end_of_action");
            myAgent.send(real_end);
        return onEnd;
    }
}
