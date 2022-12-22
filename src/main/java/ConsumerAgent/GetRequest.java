package ConsumerAgent;

import ClassXML.CFG;
import DF.DfHelper;
import additionPacakge.CheckHour;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GetRequest extends Behaviour {
    private MessageTemplate mt;
    private String distributor;
    private final CFG cfg;
    private double price;
    private CheckHour time;
    public GetRequest (double price, String distributor, CFG cfg, CheckHour time){
        this.distributor = distributor;
        this.cfg = cfg;
        this.price = price;
        this.time = time;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void action() {
        mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchProtocol("deal")),
                MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CANCEL), MessageTemplate.MatchProtocol("fail_trade")));
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null){
            log.debug("{}", msg.getContent());
            if (msg.getProtocol().equals("fail_trade") && msg.getPerformative() == 2){
                log.info("{} didn't get energy :c", myAgent.getLocalName());
                myAgent.removeBehaviour(this);
            } else if (msg.getProtocol().equals("deal") && msg.getPerformative() == 3){
                if (Double.parseDouble(msg.getContent().split(";")[1]) > price){
                    price = price*2;
                    myAgent.addBehaviour(new SendRequestForEnergyBehaviour(myAgent, cfg, time, price, distributor, "request"));
                    log.info("{} increase price to {}", myAgent.getLocalName(), price);
                    myAgent.removeBehaviour(this);
//                price = price * 2;
//                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
//                request.setProtocol("request");
//                request.setContent("2");
//                myAgent.send(request);
//                    ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
//                    distributor.forEach(m::addReceiver);
//                m.setContent((poc.getLoad()*cfg.getFullLoad())+";"+ currentTime+";"+price);
//                    m.setProtocol("request");
//                    myAgent.send(m);
                } else {
                    log.info("{} spent {} for {} amount of energy", myAgent.getLocalName(), msg.getContent().split(";")[1], msg.getContent().split(";")[0]);
                }
            }
        }
        else {block();}
    }

    @Override
    public boolean done() {
        return false;
    }
}
