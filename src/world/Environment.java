package world;

import random.Random;
import tableEntry.ContextualizedTableEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Maja on 2016-12-13.
 */
public class Environment {

    private ContextualizedTableEntry soilMoisture;
    private int clock = 0;
    private TimeOfYear[] timeOfYear = {TimeOfYear.SUMMER, TimeOfYear.AUTUMN, TimeOfYear.WINTER, TimeOfYear.SPRING};

    public Environment(ContextualizedTableEntry soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public void affectSoilMoisture(double irrigate){


        double soilMoistureValue = soilMoisture.getValue();
        double unroundedSoilMoisture = (soilMoistureValue - Random.random(0.0, 0.05)) + (irrigate * 0.05);

        /*if(irrigate == 0) {
            soilMoisture -= 0.05;
        }else{
            soilMoisture += 0.05;

        }*/

        if(soilMoistureValue < 0 ){
            soilMoistureValue = 0;
        }else if(soilMoistureValue > 1){
            soilMoistureValue = 1;
        }

       if(unroundedSoilMoisture < 0 ){
            unroundedSoilMoisture = 0;
        }else if(unroundedSoilMoisture > 1){
            unroundedSoilMoisture = 1;
        }

        BigDecimal bigDecimalSoilMoisture = new BigDecimal(unroundedSoilMoisture);
        bigDecimalSoilMoisture = bigDecimalSoilMoisture.setScale(2, RoundingMode.HALF_UP);

        soilMoisture.setValue(bigDecimalSoilMoisture.doubleValue());
        soilMoisture.setWhen(timeOfYear[clock % timeOfYear.length]);
    }

    public double getSoilMoisture(){
        return soilMoisture.getValue();
    }
}
