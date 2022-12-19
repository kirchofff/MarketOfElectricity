package additionPacakge;

import java.util.ArrayList;
import java.util.List;

public class Functions {
    private double energy;
    private List<Double> coefficients;
    private CheckHour time;
    public Functions(List <Double> coefficients, CheckHour time){
        this.coefficients = coefficients;
        this.time = time;
//        this.energy = 0;
    }

    public double raiseEnergy(){
        if (coefficients.size() == 1){
//            energy = 0;
            energy += coefficients.get(0);
            return energy;
        } else if (coefficients.size() == 2){
//            energy = 0;
            for (int i = 0; i < time.returnCurrentTime(); i++){
                energy += ((1/(coefficients.get(1)*Math.sqrt(2*Math.PI)))*Math.exp(-(Math.pow((i - coefficients.get(0)),2)/(2*Math.pow(coefficients.get(1),2)))));
            }
            energy += ((1/(coefficients.get(1))*Math.sqrt(2*Math.PI)))*energy;
            return energy;
        } else if (coefficients.size() == 4){
//            energy = 0;
            if (time.returnCurrentTime() > 5 && time.returnCurrentTime() < 19){
                energy += (coefficients.get(0)*Math.pow(time.returnCurrentTime(), 0)+coefficients.get(1)*Math.pow(time.returnCurrentTime(), 1)+coefficients.get(2)*Math.pow(time.returnCurrentTime(), 2)+coefficients.get(3)*Math.pow(time.returnCurrentTime(), 3));
                return energy;
            } else if ((time.returnCurrentTime() >= 0 && time.returnCurrentTime() <= 5) || (time.returnCurrentTime() >= 19 && time.returnCurrentTime() < 24)){
                return energy;
            }
        }
        return -10000;
    }
    public void reduceEnergy (double reduce){
        energy -= reduce;
    }
    public double returnEnergy(){
        return energy;
    }
}
