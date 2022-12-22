package DistributerAgent;

import ConsumerAgent.GetRequest;
import additionPacakge.BidsAnalyzer;
import additionPacakge.DivideBitsAnalyzer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DistributeFSM extends FSMBehaviour {
    private static final String COLLECT_BIDS="collect_bids", DEAL_WITH_WINNER="deal_with_winner", END="end", DIVIDE_CONTRACT = "divide_contract",
            DEAL_WITH_DIVIDE_WINNER="deal_with_divide_winner", UNSUCCESSFUL_TRADE="unsuccessful_trade";
    private double energyRequest;
    private double price;
    private AID topic;
    private AID consumer;
    private List <AID> seller;
    public DistributeFSM(Agent myAgent, double energyRequest, double price, AID topic, AID consumer, List <AID> seller) {
        this.myAgent = myAgent;
        this.energyRequest = energyRequest;
        this.price = price;
        this.topic = topic;
        this.consumer = consumer;
        this.seller = seller;
    }

    @SneakyThrows
    @Override
    public void onStart() {
        BidsAnalyzer bidsAnalyzer = new BidsAnalyzer();
        DivideBitsAnalyzer divideBitsAnalyzer = new DivideBitsAnalyzer();

        this.registerFirstState(new SendRequestAndCollectBids(bidsAnalyzer, energyRequest, price, topic, seller), COLLECT_BIDS);

        this.registerState(new ConcludeContract(bidsAnalyzer, energyRequest, consumer, price), DEAL_WITH_WINNER);

        this.registerState(new DivideContract(divideBitsAnalyzer, energyRequest, price, topic, seller),DIVIDE_CONTRACT);

        this.registerState(new ConcludeDividedContact(divideBitsAnalyzer, consumer, energyRequest, price),DEAL_WITH_DIVIDE_WINNER);

        this.registerState(new UnsuccessfulTrade(consumer),UNSUCCESSFUL_TRADE);



        this.registerTransition(COLLECT_BIDS, DEAL_WITH_WINNER, 1); // If bids are successful -> END

        this.registerDefaultTransition(DEAL_WITH_WINNER, END);
//        this.registerTransition(DEAL_WITH_WINNER, END, 1);
//
//        this.registerTransition(DEAL_WITH_WINNER, END, 2);

        this.registerTransition(COLLECT_BIDS, DIVIDE_CONTRACT, 2); // If bids unsuccessful -> DIVIDE

        this.registerTransition(DIVIDE_CONTRACT, DEAL_WITH_DIVIDE_WINNER, 3); // If divide successful -> END

        this.registerTransition(DEAL_WITH_DIVIDE_WINNER, END, 1);

        this.registerTransition(DEAL_WITH_DIVIDE_WINNER, END, 2);

        this.registerTransition(DIVIDE_CONTRACT, UNSUCCESSFUL_TRADE, 4); // If divide unsuccessful -> unsuccessful trade

         this.registerTransition(UNSUCCESSFUL_TRADE, END, 1);

//        this.registerDefaultTransition(UNSUCCESSFUL_TRADE, END);
//
//        this.registerDefaultTransition(DEAL_WITH_DIVIDE_WINNER, END);
//
//        this.registerDefaultTransition(DEAL_WITH_WINNER, END);

        this.registerLastState(new OneShotBehaviour() {
            @Override
            public void action() {
                log.info("Auction has been ended");
//                ACLMessage m = new ACLMessage(ACLMessage.CONFIRM);
//                seller.forEach(m::addReceiver);
//                m.setContent("");
//                m.setProtocol("end_of_action");
//                myAgent.send(m);
            }
        }, END);

    }
}
