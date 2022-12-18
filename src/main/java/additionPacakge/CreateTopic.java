package additionPacakge;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;

public class CreateTopic {
    private Agent myAgent;
    public CreateTopic (Agent myAgent){
        this.myAgent = myAgent;
    }
    public AID returnTopicName (String topic_name){
        TopicManagementHelper topicHelper = null;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper)
                    myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topic_name);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }
}
