package ProducerAgent;

import jade.core.behaviours.OneShotBehaviour;

public class ReduceEnergyBehaviour extends OneShotBehaviour {
    private double energy;
    private double reduce;
    public ReduceEnergyBehaviour(double energy, double reduce){
        this.energy = energy;
        this.reduce = reduce;
    }
    @Override
    public void action() {
        energy -= reduce;
    }
}
