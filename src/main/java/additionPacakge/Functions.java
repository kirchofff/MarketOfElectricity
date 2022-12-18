package additionPacakge;

import java.util.List;

public class Functions {
    private double energy;
    public static double returnEnergy(int time, List <Double> coeffs){
        if (coeffs.size() == 1){
            return coeffs.get(0);
        } else if (coeffs.size() == 2){
            return ((1/(coeffs.get(1)*Math.sqrt(2*Math.PI)))*Math.exp(-(Math.pow((time - coeffs.get(0)),2)/(2*Math.pow(coeffs.get(1),2)))));
        } else if (coeffs.size() == 4){
            if (time > 5 && time < 19){
                return (coeffs.get(0)*Math.pow(time, 0)+coeffs.get(1)*Math.pow(time, 1)+coeffs.get(2)*Math.pow(time, 2)+coeffs.get(3)*Math.pow(time, 3));
            } else if ((time >= 0 && time <= 5) || (time >= 19 && time < 24)){
                return 0;
            }
        }
        return -10000;
    }
}
