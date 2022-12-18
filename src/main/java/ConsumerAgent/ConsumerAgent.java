package ConsumerAgent;

import ClassXML.CFG;
import ClassXML.ParametersOfConsumer;
import DF.DfHelper;
import additionPacakge.CheckHour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConsumerAgent extends Behaviour {
    private final CFG cfg;
    private MessageTemplate mt;
    private boolean isTimeOut = true;
    private CheckHour time;
    private int currentTime;
    public ConsumerAgent (Agent myAgent, CFG cfg, CheckHour time){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.time = time;
        this.currentTime = time.returnCurrentTime();
    }


    @Override
    public void onStart() {


    }
    @SneakyThrows
    @Override
    public void action() {
        mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){

        }else {
            block();
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
