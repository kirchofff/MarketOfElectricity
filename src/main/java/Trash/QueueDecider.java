package Trash;

import jade.core.AID;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class QueueDecider {

    private  List <AID> senders = new ArrayList<>();
    @Synchronized
    public synchronized void addSender (AID sender){
        if (!senders.contains(sender)){
            senders.add(sender);
        }
    }
    public void clearSenders (){
        senders.clear();
    }
    public List<AID> getSender(){
        return senders;
    }
}
