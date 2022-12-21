package additionPacakge;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
public class BidsAnalyzer {
    private double bestValue = Double.MAX_VALUE;
    private AID bestSeller;

    private boolean success;

    public void putBid(ACLMessage m, double requestEnergy){
        String content = m.getContent();
        double price;
        double energy;
        try{
            price = Double.parseDouble(content.split(";")[2]);
            energy = Double.parseDouble(content.split(";")[1]);
            price = price*requestEnergy;
        } catch (NumberFormatException nfe){
            log.error("inappropriate content");
            return;
        }
        if (energy <= 0){
//            log.debug("{} received refuse with price {} for this amount of power {}", content.split(";")[0], price, energy);
            return;
        } else {
//            log.debug("{} received propose with price {} for this amount of power {}", content.split(";")[0], price, energy);
        }
        if (price < bestValue && energy >= requestEnergy){
            bestValue = price;
            bestSeller = m.getSender();
//            log.debug("BidsAnalyzer best value {} best seller",bestValue, bestSeller.getLocalName());
        }
    }

    public Optional<AID> getBestSeller(){
        return bestSeller == null ? Optional.empty() : Optional.of(bestSeller);
    }
    public double getBestValue() {
        return bestValue;
    }
    public void resetBest (){
        bestSeller = null;
        bestValue = Double.MAX_VALUE;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
