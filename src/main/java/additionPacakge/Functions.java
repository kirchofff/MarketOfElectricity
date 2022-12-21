package additionPacakge;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Functions {
    private double energy;
    private List<Double> coefficients;
    private CheckHour time;
    private Random random = new Random();
    public Functions(List <Double> coefficients, CheckHour time){
        this.coefficients = coefficients;
        this.time = time;
        energy = 0;
//        this.energy = 0;
    }

    public double raiseEnergy(){
        if (coefficients.size() == 1){
//            energy = 0;
            energy += coefficients.get(0);
            return energy;
        } else if (coefficients.size() == 2){
//            energy = 0;
            double tempEnergy = BigDecimal.valueOf(random.nextGaussian() * coefficients.get(1)+coefficients.get(0)).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (tempEnergy < 0){
                energy += 0;
            } else if (tempEnergy > 0){
                energy += tempEnergy;
            }
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
