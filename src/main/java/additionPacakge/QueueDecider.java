package additionPacakge;

import jade.core.AID;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class QueueDecider {

    private List <AID> senders = new ArrayList<>();
    public void addSender (AID sender){
        if (!senders.contains(sender)){
            senders.add(sender);
        }
    }
    public void clearSenders (){
        senders.clear();
    }
    public AID decideSender(){
        AID senderToReturn = senders.get(0);
        return senderToReturn;
    }
}
