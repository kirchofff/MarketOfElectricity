package additionPacakge;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
@Slf4j
public class BidsAnalyzer {
    private double bestValue;
    private AID bestSeller;

    private boolean success;

    public void putBid(ACLMessage m){
        String content = m.getContent();
        double value;
        try{
            value = Double.parseDouble(content);
        } catch (NumberFormatException nfe){
            log.error("inappropriate content");
            return;
        }
        if (value <= 0){
            System.out.println("received refuse with value "+value);
            return;
        } else {
            System.out.println("received propose with value "+value);
        }
        if (bestSeller == null || value < bestValue){
            bestValue = value;
            bestSeller = m.getSender();
        }
    }

    public Optional<AID> getBestSeller(){
        return bestSeller == null ? Optional.empty() : Optional.of(bestSeller);
    }

    public double getBestValue() {
        return bestValue;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
