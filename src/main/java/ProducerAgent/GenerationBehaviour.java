package ProducerAgent;

import ClassXML.CFGGeneration;
import ClassXML.ParametersOfGeneration;
import DF.DfHelper;
import additionPacakge.CheckHour;
import additionPacakge.Functions;
import jade.core.behaviours.Behaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class GenerationBehaviour extends Behaviour {
//    private final CFGGeneration cfgGeneration;
    private List<Double> coefficients = new ArrayList<>();
    private CheckHour time;
    private Functions energy;
    private double currentEnergy;
    private int timeOfGeneration;
//    private Functions functions = new Functions();
    public GenerationBehaviour(CheckHour time, Functions energy){
//        this.cfgGeneration = cfg;
        this.time = time;
        this.energy = energy;
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Seller");
        timeOfGeneration = time.returnCurrentTime();
        currentEnergy = energy.raiseEnergy();
        log.info("{} have this amount of energy {} at {} o'clock", myAgent.getLocalName(), currentEnergy, time.returnCurrentTime());
//        if (cfgGeneration.getName().equals("SunElectricStation")){
//            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
//                coefficients.add(parameters.getCoef());
//            }
//        }else if (cfgGeneration.getName().equals("WindElectricStation")){
//            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
//                coefficients.add(parameters.getCoef());
//            }
//        }else if (cfgGeneration.getName().equals("ThermalElectricStation")){
//            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
//                coefficients.add(parameters.getCoef());
//            }
//        }
//        functions.raiseEnergy(time.returnCurrentTime(), coefficients);
//        log.info("{} have this amount of energy {} at {} o'clock", cfgGeneration.getName(), energy, time.returnCurrentTime());
    }

    @Override
    public void action() {
//        if (timeOfGeneration != time.returnCurrentTime()){
//            currentEnergy = energy.raiseEnergy();
//            log.info("{} have this amount of energy {} at {} o'clock", myAgent.getLocalName(), energy, time.returnCurrentTime());
//            timeOfGeneration = time.returnCurrentTime();
//
//        }
//        while (time.returnCurrentTime() < 24 && time.returnCurrentTime() >= 0){

//            log.info("{} have this amount of energy {} at {} o'clock", cfgGeneration.getName(), energy, time.returnCurrentTime());
//            while (time.returnCurrentTime() == timeOfGeneration){}
//        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
