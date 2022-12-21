package DistributerAgent;

import jade.core.behaviours.Behaviour;

public class SendAcceptRoGeneration extends Behaviour {
    @Override
    public void action() {
        // Если конракт точно заключили, то убавляем количество энергии
    }

    @Override
    public boolean done() {
        return false;
    }
}
