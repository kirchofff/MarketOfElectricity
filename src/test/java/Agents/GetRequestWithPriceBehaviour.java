package Agents;

import ClassXML.CFG;
import additionPacakge.CheckHour;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetRequestWithPriceBehaviour extends OneShotBehaviour {
    private int onEnd = 0;
    private double price;
    public GetRequestWithPriceBehaviour(double price) {
        this.price = price;
    }

    @Override
    public void onStart() {
        log.info("{} was born", myAgent.getLocalName());
    }

    @Override
            public void action() {
                MessageTemplate mt;
                mt = MessageTemplate.or(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchProtocol("deal")),
                        MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CANCEL), MessageTemplate.MatchProtocol("fail_trade")));
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null){
                    if (msg.getProtocol().equals("deal") && msg.getPerformative() == 3){
                        if (Double.parseDouble(msg.getContent().split(";")[1]) > price){
                            log.info("{} increase price to {}", myAgent.getLocalName(), price);
                            onEnd = 1;
                        }
                    }
                }
            }

            @Override
            public int onEnd() {
                return onEnd;
            }
}
