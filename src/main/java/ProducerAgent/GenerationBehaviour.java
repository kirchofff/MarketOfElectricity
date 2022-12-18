package ProducerAgent;

import ClassXML.CFGGeneration;
import ClassXML.ParametersOfGeneration;
import DF.DfHelper;
import additionPacakge.CheckHour;
import additionPacakge.Functions;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.List;

public class GenerationBehaviour extends Behaviour {
    private final CFGGeneration cfgGeneration;
    private List<Double> coefficients = new ArrayList<>();
    private CheckHour time;
    private double energy = 0;
    private int timeOfGeneration;
    public GenerationBehaviour(CFGGeneration cfg, CheckHour time){
        this.cfgGeneration = cfg;
        this.time = time;
    }

    @Override
    public void onStart() {
        DfHelper.registerAgent(myAgent, "Seller");
        if (cfgGeneration.getName().equals("SunElectricStation")){
            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
                coefficients.add(parameters.getCoef());
            }
        }else if (cfgGeneration.getName().equals("WindElectricStation")){
            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
                coefficients.add(parameters.getCoef());
            }
        }else if (cfgGeneration.getName().equals("ThermalElectricStation")){
            for (ParametersOfGeneration parameters : cfgGeneration.getCoefficients()) {
                coefficients.add(parameters.getCoef());
            }
        }

    }

    @Override
    public void action() {
        while (time.returnCurrentTime() < 24 && time.returnCurrentTime() >= 0){
            timeOfGeneration = time.returnCurrentTime();
            energy += Functions.returnEnergy(time.returnCurrentTime(), coefficients);
//            log.info("{} have this amount of energy {} at {} o'clock", cfgGeneration.getName(), energy, time.returnCurrentTime());
            while (time.returnCurrentTime() == timeOfGeneration){}
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
