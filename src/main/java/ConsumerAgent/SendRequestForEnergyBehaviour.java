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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SendRequestForEnergyBehaviour extends OneShotBehaviour {
    private final CFG cfg;
    private MessageTemplate mt;
    private CheckHour currentTime;
    private List<AID> distributor;
    private double price;
    private String distributorName;
    private String requestName;
    public SendRequestForEnergyBehaviour(Agent myAgent, CFG cfg, CheckHour currentTime, double price, String distributorName, String requestName){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.currentTime = currentTime;
        this.price = price;
        this.distributorName = distributorName;
        this.requestName = requestName;
    }
    @SneakyThrows
    @Override
    public void onStart() {
//        Thread.sleep(100);
//        distributor = new ArrayList<>(DfHelper.findAgents(myAgent, "DistributorOf"+distributorName));
    }
    @SneakyThrows
    @Override
    public void action() {
        for (ParametersOfConsumer poc : cfg.getPeriods()) {
            if (poc.getTime() == currentTime.returnCurrentTime()){
                ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
//                distributor.forEach(m::addReceiver);
                AID aid = new AID("Distributor_Of_"+distributorName, false);
                m.addReceiver(aid);
                m.setContent((poc.getLoad()*cfg.getFullLoad())+";"+ currentTime.returnCurrentTime()+";"+price);
                m.setProtocol(requestName);
                myAgent.send(m);
                myAgent.addBehaviour(new GetRequest(price, distributorName, cfg, currentTime));
//                log.info("{} send request to energy {} MWt at {} o'clock to {}", myAgent.getLocalName(),
//                        poc.getLoad()*cfg.getFullLoad(),
//                        currentTime.returnCurrentTime(),
//                        aid.getLocalName());
            }
        }
    }

}
