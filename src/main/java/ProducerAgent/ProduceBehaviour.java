package ProducerAgent;

import ClassXML.CFGGeneration;
import ClassXML.ParametersOfGeneration;
import additionPacakge.CheckHour;
import additionPacakge.Functions;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProduceBehaviour extends FSMBehaviour {
    private final CFGGeneration cfgGeneration;
    private CheckHour time;
    private final String START_GENERATING = "start_generating", RECEIVE_MSG = "receive_msg", REDUCE_ENERGY="reduce_energy";
    private double energy;
    private double reduce = 0;
    private List<Double> coefficients = new ArrayList<>();
    public ProduceBehaviour(Agent myAgent, CFGGeneration cfg, CheckHour time){
        this.myAgent = myAgent;
        this.cfgGeneration = cfg;
        this.time = time;
    }

    @Override
    public void onStart() {
        for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
            coefficients.add(parameters.getCoef());
        }
        Functions functions = new Functions(coefficients, time);
        myAgent.addBehaviour(new GenerationBehaviour(time, functions));
//        this.registerFirstState(new GenerationBehaviour(time, functions), START_GENERATING);
        this.registerFirstState(new RecieveMsgWithTopicEnergyPrice(), RECEIVE_MSG);
        this.registerDefaultTransition(START_GENERATING, RECEIVE_MSG);
//        this.registerState(new ReduceEnergyBehaviour(energy, reduce), REDUCE_ENERGY);
//        this.registerTransition(START_GENERATING, RECEIVE_MSG, 1);

    }

}
