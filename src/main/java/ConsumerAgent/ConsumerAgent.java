package ConsumerAgent;

import ClassXML.CFG;
import ClassXML.ParametersOfConsumer;
import DF.DfHelper;
import additionPacakge.CheckHour;
import additionPacakge.CreateTopic;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ConsumerAgent extends Behaviour {
    private final CFG cfg;
    private MessageTemplate mt;
    private CheckHour time;
    private int currentTime;
    private int price = 150;

    public ConsumerAgent (Agent myAgent, CFG cfg, CheckHour time){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.time = time;
        this.currentTime = time.returnCurrentTime();
    }


    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "ConsumerAgent");
        myAgent.addBehaviour(new SendRequestForEnergyBehaviour(myAgent,cfg, time, price, myAgent.getLocalName(), "request"));
        currentTime = time.returnCurrentTime();
    }
    @Override
    public void action() {
        if (currentTime != time.returnCurrentTime()){
            myAgent.addBehaviour(new SendRequestForEnergyBehaviour(myAgent,cfg, time, price, myAgent.getLocalName(),"request"));
            currentTime = time.returnCurrentTime();
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
