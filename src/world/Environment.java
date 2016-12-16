package world;

import tableEntry.ContextualizedTableEntry;
import tableEntry.NamedContextualizedTableEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Maja on 2016-12-13.
 */
public class Environment {

    private ContextualizedTableEntry soilMoistureWest;
    private ContextualizedTableEntry soilMoistureEast;
    public static int clock = 0;
    private int timeOfYearCounter = 0;
    private TimeOfYear[] timeOfYear = {TimeOfYear.SUMMER, TimeOfYear.AUTUMN, TimeOfYear.WINTER, TimeOfYear.SPRING};

    public Environment(double initialSoilMoisture, TimeOfYear timeOfYear) {
        this.soilMoistureEast = new NamedContextualizedTableEntry(initialSoilMoisture, timeOfYear, "Irrigator", "Soil Moisture EAST");
        this.soilMoistureWest = new NamedContextualizedTableEntry(initialSoilMoisture, timeOfYear, "Irrigator", "Soil Moisture WEST");
    }

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public void affectSoilMoisture(double irrigate){

        calculateSoilMoisture(soilMoistureEast, irrigate);
        calculateSoilMoisture(soilMoistureWest, irrigate);

        if(timeOfYearCounter % (365/4) == 0) {
            clock++;
        }
        timeOfYearCounter++;

    }

    private void calculateSoilMoisture(ContextualizedTableEntry initialSoilMoisture, double irrigate) {

        double soilMoistureValue = initialSoilMoisture.getValue();
        double unroundedSoilMoisture = (soilMoistureValue - random(0.0, 0.05)) + (irrigate * 0.05);

        if(unroundedSoilMoisture < 0 ){
             unroundedSoilMoisture = 0;
         }else if(unroundedSoilMoisture > 1){
             unroundedSoilMoisture = 1;
         }

      /*  BigDecimal bigDecimalSoilMoisture = new BigDecimal(unroundedSoilMoisture);
        bigDecimalSoilMoisture = bigDecimalSoilMoisture.setScale(2, RoundingMode.HALF_UP);

        initialSoilMoisture.setValue(bigDecimalSoilMoisture.doubleValue()); */

        initialSoilMoisture.setValue(unroundedSoilMoisture);

        initialSoilMoisture.setWhen(timeOfYear[clock % timeOfYear.length]);
    }

    public ContextualizedTableEntry getSoilMoisture(String ID){

        switch (ID){
            case "Soil Moisture WEST" :
                return soilMoistureWest;
            case "Soil Moisture EAST" :
                return soilMoistureEast;
            default:
                return null;
        }
    }
}
