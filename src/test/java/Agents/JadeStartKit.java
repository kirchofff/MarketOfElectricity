package Agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.Behaviour;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import lombok.SneakyThrows;

public class JadeStartKit {
    private AgentContainer agentContainer;
    private AgentController newAgent;
    public void startJade (){
        Properties properties = new Properties();
        properties.setProperty("gui","true");
        ProfileImpl profile = new ProfileImpl(properties);
        Runtime.instance().setCloseVM(true);
        agentContainer = Runtime.instance().createMainContainer(profile);
    }
    @SneakyThrows
    public String createAgent (String agent, Behaviour... behaviours){
        newAgent = agentContainer.createNewAgent(agent, MockAgent.class.getName(), behaviours);
        newAgent.start();
        return newAgent.getName();
    }
}
