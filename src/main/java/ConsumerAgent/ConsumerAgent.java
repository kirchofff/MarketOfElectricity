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
    private String distributor;
    public ConsumerAgent (Agent myAgent, CFG cfg, CheckHour time, String distributor){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.time = time;
        this.currentTime = time.returnCurrentTime();
        this.distributor = distributor;
    }


    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "ConsumerAgent");
        myAgent.addBehaviour(new SendRequestForEnergyBehaviour(myAgent,cfg, time.returnCurrentTime(), price, distributor));
        currentTime = time.returnCurrentTime();
    }
    @Override
    public void action() {
        if (currentTime != time.returnCurrentTime()){
            myAgent.addBehaviour(new SendRequestForEnergyBehaviour(myAgent,cfg, time.returnCurrentTime(), price, distributor));
            currentTime = time.returnCurrentTime();
        }
        mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchProtocol("deal"));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){
            if (Integer.parseInt(msg.getContent().split(";")[1]) > price){
                price = price * 2;
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.setProtocol("request");
                request.setContent("2");
                myAgent.send(request);
            } else {
                log.info("{} spent {} for {} amount of energy", myAgent.getLocalName(), price, msg.getContent().split(";")[0]);
                price = 150;
            }

        }else {block();}

    }

    @Override
    public boolean done() {
        return false;
    }
}
