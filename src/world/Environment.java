package world;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Maja on 2016-12-13.
 */
public class Environment {

    private double soilMoisture = 0.2;

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public void affectSoilMoisture(double irrigate){

        double unroundedSoilMoisture = (soilMoisture - random(0.0, 0.05)) + (irrigate * 0.05);

        if(unroundedSoilMoisture < 0 ){
            unroundedSoilMoisture = 0;
        }else if(unroundedSoilMoisture > 1){
            unroundedSoilMoisture = 1;
        }

        BigDecimal bigDecimalSoilMoisture = new BigDecimal(unroundedSoilMoisture);
        bigDecimalSoilMoisture = bigDecimalSoilMoisture.setScale(2, RoundingMode.HALF_UP);

        soilMoisture = bigDecimalSoilMoisture.doubleValue();
    }

    public double getSoilMoisture(){
        return soilMoisture;
    }
}
