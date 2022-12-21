package ProducerAgent;

import Topic.SendToTopic;
import additionPacakge.Functions;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerationFSM extends FSMBehaviour {
    private AID subscribeOnTopic;
    private Functions functions;
    private double price;
    private int performative;
    private String protocol;
    private double energy;
    private String RECEIVE_RESULT = "receive_result", END = "end", UNSUCCESSFUL_TRADE= "unsuccessful_trade",
            FIRST_SEND_TO_TOPIC = "first_send_to_topic", RECEIVE_DIVIDED_RESULT = "receive_divided_result",
            SEND_TO_TOPIC = "send_to_topic";
    public GenerationFSM (Functions functions, AID subscribeOnTopic, double price, int performative, String protocol, double energy){
        this.functions = functions;
        this.subscribeOnTopic = subscribeOnTopic;
        this.protocol = protocol;
        this.performative = performative;
        this. price = price;
        this.energy = energy;
    }
    @Override
    public void onStart() {
        // Send first time at Prod Beh
        this.registerFirstState(new RecieveResultOfAuction(functions), RECEIVE_RESULT);

        this.registerState(new SendToTopic(myAgent, subscribeOnTopic, energy,price, performative, protocol), SEND_TO_TOPIC);

        this.registerState(new ReceiveDividedResult(functions, myAgent, subscribeOnTopic, energy,price, performative, protocol), RECEIVE_DIVIDED_RESULT);

        this.registerTransition(RECEIVE_RESULT, END, 1); // if generation receive positive accept -> END

        this.registerTransition(RECEIVE_RESULT, RECEIVE_DIVIDED_RESULT, 0); // if generation receive negative accept -> send try divide

//        this.registerTransition(SEND_TO_TOPIC, RECEIVE_DIVIDED_RESULT, 1); // send to topic always return 1

        this.registerTransition(RECEIVE_DIVIDED_RESULT, END, 1); // end anyway

        this.registerTransition(RECEIVE_DIVIDED_RESULT, END, 0);

//        this.registerTransition(FIRST_SEND_TO_TOPIC, RECEIVE_RESULT, 1); // send price to topic first time
//        this.registerTransition(RECEIVE_RESULT, END, 1);// if generation receive positive accept -> END
//        this.registerTransition(RECEIVE_RESULT, SEND_TO_TOPIC, 0); // if generation receive negative accept -> send to topic again to divide
//        this.registerTransition(RECEIVE_DIVIDED_RESULT, END, 1); // end anyway
//        this.registerTransition(RECEIVE_DIVIDED_RESULT, END, 0);
        this.registerLastState(new OneShotBehaviour() {
            @Override
            public void action() {
//                log.info("Auction has been ended");
            }
        }, END);
    }
}
