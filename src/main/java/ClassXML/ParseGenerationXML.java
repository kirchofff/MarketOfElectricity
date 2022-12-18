package ClassXML;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
@Slf4j
public class ParseGenerationXML {
    public static CFGGeneration ParseXML(String name) {
        final String path = "C:/Users/vladg/Desktop/M_1_sem/Java/Labki/MarketOfElectricity/MarketOfElectricity/src/main/java/Configuration/";
        CFGGeneration cfg = null;
        {
            try {
                JAXBContext context = JAXBContext.newInstance(CFGGeneration.class);
                Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
                cfg = (CFGGeneration) jaxbUnmarshaller.unmarshal(new File(path+name+".xml"));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return (cfg);
    }
}
