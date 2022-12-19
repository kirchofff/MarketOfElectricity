package ClassXML;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cfg")
@Data
public class CFGGeneration {

    @XmlElement
    private String name;

    @XmlElement
    private int price;

    @XmlElementWrapper(name = "coefficients")
    @XmlElement(name = "coefficient")
    private List <ParametersOfGeneration> coefficients;

}