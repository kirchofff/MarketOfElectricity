package Agents;

import jade.core.behaviours.Behaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyBeh extends Behaviour {
    @Override
    public void onStart() {
        log.info("{} started",myAgent.getLocalName());
    }

    @Override
    public void action() {
    }

    @Override
    public boolean done() {
        return false;
    }
}
