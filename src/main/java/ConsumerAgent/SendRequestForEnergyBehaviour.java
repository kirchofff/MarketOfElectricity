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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SendRequestForEnergyBehaviour extends Behaviour {
    private final CFG cfg;
    private MessageTemplate mt;
    private CheckHour time;
    private List<AID> distributor;
    public SendRequestForEnergyBehaviour(Agent myAgent, CFG cfg, CheckHour time){
        this.cfg = cfg;
        this.myAgent = myAgent;
        this.time = time;
    }
    @SneakyThrows
    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "ConsumerAgent");
        distributor = new ArrayList<>(DfHelper.findAgents(myAgent, "Distributor"));
    }

    @Override
    public void action() {
        while (true){
            for (ParametersOfConsumer poc : cfg.getPeriods()) {
                if (poc.getTime() == time.returnCurrentTime()){
                        //                        log.info("Agent send request to energy {} MWt at {} o'clock", poc.getLoad(), time.returnCurrentTime());
                        ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
                        distributor.forEach(m::addReceiver);
                        m.setContent((poc.getLoad()*cfg.getFullLoad())+";"+time.returnCurrentTime()+";"+"150");
                        m.setProtocol("request");
                        myAgent.send(m);
                }
                while (poc.getTime() == time.returnCurrentTime()){}
            }
        }

    }

    @Override
    public boolean done() {
        return false;
    }
}
