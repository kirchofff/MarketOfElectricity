package additionPacakge;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
public class DivideBitsAnalyzer {
    private List <Double> fullPrice;
    private List <AID> seller;
    private double currentEnergy;
    private List <Double> energyOfSenders;
    private boolean success = false;

    public DivideBitsAnalyzer() {
        this.fullPrice = new ArrayList<>();
        this.seller = new ArrayList<>();
        this.energyOfSenders = new ArrayList<>();
    }

    public void putBid(ACLMessage m, double requestEnergy){
        String content = m.getContent();
        double price;
        double energy;
        try{
            price = Double.parseDouble(content.split(";")[2]);
            energy = Double.parseDouble(content.split(";")[1]);
        } catch (NumberFormatException nfe){
            log.error("inappropriate content");
            return;
        }
        if (energy <= 0){
//            log.info("{} received refuse with price {} for this amount of power {}", content.split(";")[0], price, energy);
            return;
        } else {
//            log.info("{} received propose with price {} for this amount of power {}", content.split(";")[0], price, energy);
        }
        if (currentEnergy < requestEnergy || currentEnergy == 0){
            fullPrice.add(price);
            currentEnergy += energy;
            energyOfSenders.add(energy);
            seller.add(m.getSender());
//            log.debug("DivideBitsAnalyzer have this amount of energy {}", currentEnergy);
//            log.debug("DivideBitsAnalyzer have this list with senders {}", seller.toString());
        }
        if (currentEnergy > requestEnergy){
            success = true;
        }
    }
    public List <AID> getSellers(){
        return seller;
    }
    public List <Double> getFullPrice() {
        return fullPrice;
    }
    public List <Double> getEnergyOfSenders (){return energyOfSenders;}
    public boolean isSuccess() {return success;}
    public void resetDivideContract (){
        fullPrice.clear();
        seller.clear();
        energyOfSenders.clear();
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
