package ConsumerAgent;

import ClassXML.CFG;
import ClassXML.ParametersOfConsumer;
import DF.DfHelper;
import additionPacakge.CheckHour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;


public class SendRequestForEnergyBehaviour extends OneShotBehaviour {
    private final CFG cfg;
    private MessageTemplate mt;
    private int currentTime;
    private List<AID> distributor;
    private int price;
    private String distributorName;
    public SendRequestForEnergyBehaviour(Agent myAgent, CFG cfg, int currentTime, int price, String distributorName){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.currentTime = currentTime;
        this.price = price;
        this.distributorName = distributorName;
    }
    @SneakyThrows
    @Override
    public void onStart() {
        Thread.sleep(100);
        distributor = new ArrayList<>(DfHelper.findAgents(myAgent, "DistributorOf"+distributorName));
    }
    @Override
    public void action() {
        for (ParametersOfConsumer poc : cfg.getPeriods()) {
            if (poc.getTime() == currentTime){
                //                        log.info("Agent send request to energy {} MWt at {} o'clock", poc.getLoad(), time.returnCurrentTime());
                ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
                distributor.forEach(m::addReceiver);
                m.setContent((poc.getLoad()*cfg.getFullLoad())+";"+ currentTime+";"+price);
                m.setProtocol("request");
                myAgent.send(m);
            }
        }
    }

}
