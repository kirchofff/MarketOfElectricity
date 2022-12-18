package Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class MockAgent extends Agent {
    @Override
    protected void setup() {
        Object [] arguments = getArguments();
        for (Object arg : getArguments()){
            Behaviour behaviour = (Behaviour) arg;
            this.addBehaviour(behaviour);
        }
    }
}
