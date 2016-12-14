package world;

import random.Random;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Maja on 2016-12-13.
 */
public class Environment {

    private double soilMoisture;

    public Environment(double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public void affectSoilMoisture(double irrigate){

        double unroundedSoilMoisture = (soilMoisture - Random.random(0.0, 0.05)) + (irrigate * 0.05);

        /*if(irrigate == 0) {
            soilMoisture -= 0.05;
        }else{
            soilMoisture += 0.05;

        }*/

        if(soilMoisture < 0 ){
            soilMoisture = 0;
        }else if(soilMoisture > 1){
            soilMoisture = 1;
        }

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
