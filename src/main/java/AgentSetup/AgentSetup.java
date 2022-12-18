package AgentSetup;

import ClassXML.CFG;
import ClassXML.CFGGeneration;
import ClassXML.ParseGenerationXML;
import ClassXML.ParseXML;
import ConsumerAgent.SendRequestForEnergyBehaviour;
import DistributerAgent.DistributeAgent;
import ProducerAgent.ProduceBehaviour;
import Topic.TopicReceive;
import additionPacakge.CheckHour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;


public class AgentSetup extends Agent {
    private CheckHour time = new CheckHour(System.currentTimeMillis(), 10_000);
    private boolean oneTopicExist = true;
    @SneakyThrows
    @Override
    protected void setup() {
        if (oneTopicExist){
            oneTopicExist = false;
            addBehaviour(new TopicReceive());
        }
        if (getLocalName().equals("BootFactory")){
            CFG cfgBoot1 = ParseXML.ParseXML("BootFactory");
            addBehaviour(new SendRequestForEnergyBehaviour(this, cfgBoot1, time));
        }
        if (getLocalName().equals("DistributeAgent")){
            addBehaviour(new DistributeAgent(this));
        }
        if (getLocalName().equals("SES")){
            CFGGeneration cfgGeneration1 = ParseGenerationXML.ParseXML("SES");
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time));
        }
        if (getLocalName().equals("WES")){
            CFGGeneration cfgGeneration1 = ParseGenerationXML.ParseXML("WES");
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time));
        }
        if (getLocalName().equals("TES")){
            CFGGeneration cfgGeneration1 = ParseGenerationXML.ParseXML("TES");
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time));
        }
    }
}
