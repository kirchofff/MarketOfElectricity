package additionPacakge;

import jade.lang.acl.ACLMessage;
import lombok.Data;

import java.util.List;

@Data
public class TopicInformation {
    private List <Double> thermEnergy;
    private List<Integer> thermPrice;
    private ACLMessage answer;
}
