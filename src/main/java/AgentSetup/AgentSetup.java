package AgentSetup;

import ClassXML.CFG;
import ClassXML.CFGGeneration;
import ClassXML.ParseGenerationXML;
import ClassXML.ParseXML;
import ConsumerAgent.ConsumerAgent;
import DistributerAgent.DistributeAgent;
import ProducerAgent.ProduceBehaviour;
import additionPacakge.CheckHour;

import jade.core.AID;
import jade.core.Agent;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AgentSetup extends Agent {
    private CheckHour time = new CheckHour(System.currentTimeMillis(), 20_000);
    private Map <AID,Integer> queue1 = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    protected void setup() {
        List <AID> queue = new ArrayList<>();
//        if (oneTopicExist){
//            oneTopicExist = false;
//            addBehaviour(new TopicReceive());
//        }
        if (getLocalName().equals("BootFactory")){
            CFG cfgBoot1 = ParseXML.ParseXML("BootFactory");
            addBehaviour(new ConsumerAgent(this, cfgBoot1, time));
            Thread.sleep(300);
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
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time, queue));
        }
        if (getLocalName().equals("WES")){
            CFGGeneration cfgGeneration1 = ParseGenerationXML.ParseXML("WES");
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time, queue));
        }
        if (getLocalName().equals("TES")){
            CFGGeneration cfgGeneration1 = ParseGenerationXML.ParseXML("TES");
            addBehaviour(new ProduceBehaviour(this, cfgGeneration1, time, queue));
        }
    }
}
