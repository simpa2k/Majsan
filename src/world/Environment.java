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
    private ContextualizedTableEntry temperature;
    private ContextualizedTableEntry uvLight;
    private ContextualizedTableEntry windSpeed;

    public static int clock = 0;
    private int timeOfYearCounter = 0;
    private TimeOfYear[] timeOfYear = {TimeOfYear.SUMMER, TimeOfYear.AUTUMN, TimeOfYear.WINTER, TimeOfYear.SPRING};

    public Environment(double initialSoilMoisture, double initialTemperature, double initialUvLight, double initialWindSpeed,  TimeOfYear timeOfYear) {
        this.soilMoistureEast = new NamedContextualizedTableEntry(initialSoilMoisture, timeOfYear, "Irrigator", "Soil Moisture EAST");
        this.soilMoistureWest = new NamedContextualizedTableEntry(initialSoilMoisture, timeOfYear, "Irrigator", "Soil Moisture WEST");
        this.temperature = new NamedContextualizedTableEntry(initialTemperature, timeOfYear, "Irrigator", "Temperature");
        this.uvLight = new NamedContextualizedTableEntry(initialUvLight, timeOfYear, "Irrigator", "UV Light");
        this.windSpeed = new NamedContextualizedTableEntry(initialWindSpeed, timeOfYear, "Irrigator", "Wind Speed");

    }

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public void affectSoilMoisture(double irrigate){

        calculateSoilMoisture(soilMoistureEast, irrigate);
        calculateSoilMoisture(soilMoistureWest, irrigate);

        temperature.setWhen(timeOfYear[clock % timeOfYear.length]);
        uvLight.setWhen(timeOfYear[clock % timeOfYear.length]);
        windSpeed.setWhen(timeOfYear[clock % timeOfYear.length]);

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

    public ContextualizedTableEntry getSensorLevels(String ID){

        switch (ID){
            case "Soil Moisture WEST" :
                return soilMoistureWest;
            case "Soil Moisture EAST" :
                return soilMoistureEast;
            case "Temperature" :
                return temperature;
            case "UV Light" :
                return uvLight;
            case "Wind Speed" :
                return windSpeed;
            default:
                return null;
        }
    }
}
