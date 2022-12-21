package AgentSetup;

import ClassXML.CFG;
import ClassXML.CFGGeneration;
import ClassXML.ParseGenerationXML;
import ClassXML.ParseXML;
import ConsumerAgent.ConsumerAgent;
import DistributerAgent.DistributeAgent;
import ProducerAgent.ProduceBehaviour;
import additionPacakge.CheckHour;

import jade.core.Agent;

import lombok.SneakyThrows;


public class AgentSetup extends Agent {
    private CheckHour time = new CheckHour(System.currentTimeMillis(), 20_000);
    private boolean oneTopicExist = true;
    @SneakyThrows
    @Override
    protected void setup() {
//        if (oneTopicExist){
//            oneTopicExist = false;
//            addBehaviour(new TopicReceive());
//        }
        if (getLocalName().equals("BootFactory")){
            CFG cfgBoot1 = ParseXML.ParseXML("BootFactory");
            addBehaviour(new ConsumerAgent(this, cfgBoot1, time));
        }
        if (getLocalName().equals("CityTransport")){
            CFG cfgBoot1 = ParseXML.ParseXML("CityTransport");
            addBehaviour(new ConsumerAgent(this, cfgBoot1, time));
        }
        if (getLocalName().equals("DistributorOfBootFactory")){
            addBehaviour(new DistributeAgent(this));
        }

        if (getLocalName().equals("DistributorOfCityTransport")){
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
