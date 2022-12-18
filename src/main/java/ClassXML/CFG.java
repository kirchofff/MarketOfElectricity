package ClassXML;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cfg")
@Data
public class CFG {

    @XmlElement
    private String name;

    @XmlElement
    private double fullLoad;

    @XmlElementWrapper(name = "periods")
    @XmlElement(name = "period")
    private List <ParametersOfConsumer> periods;

}