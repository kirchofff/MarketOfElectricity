package ProducerAgent;

import ClassXML.CFGGeneration;
import additionPacakge.CheckHour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProduceBehaviour extends FSMBehaviour {
    private final CFGGeneration cfgGeneration;
    private CheckHour time;
    private final String START_GENERATING = "start_generating", RECEIVE_MSG = "receive_msg";
    public ProduceBehaviour(Agent myAgent, CFGGeneration cfg, CheckHour time){
        this.myAgent = myAgent;
        this.cfgGeneration = cfg;
        this.time = time;
    }

    @Override
    public void onStart() {
//        this.registerFirstState(new GenerationBehaviour(cfgGeneration, time), START_GENERATING);
        this.registerFirstState(new RecieveMsgWithTopicEnergyPrice(), RECEIVE_MSG);
    }

}
