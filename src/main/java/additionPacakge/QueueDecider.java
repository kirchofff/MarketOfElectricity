package additionPacakge;

import jade.core.AID;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class QueueDecider {

    private  List <AID> senders = new ArrayList<>();
    @Synchronized
    @SneakyThrows
    public synchronized void addSender (AID sender){
        if (!senders.contains(sender)){
            senders.add(sender);
//            log.debug("Add to queue {}", senders.toString());
        }
    }
    @Synchronized
    public synchronized AID getFirst (){
        return senders.get(0);
    }
    @Synchronized
    public synchronized void clearSenders (){
        senders.clear();
    }
    @Synchronized
    public synchronized List<AID> getSenders(){
        return senders;
    }
    @Synchronized
    public synchronized void removeSender (AID sender){
        senders.remove(sender);
    }
}
