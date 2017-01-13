package field;

import tableEntry.ContextualizedTableEntry;
import tableEntry.NamedContextualizedTableEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

        calculateTemperature();
        calculateUvLight();
        calculateWindSpeed();

        calculateSoilMoisture(soilMoistureEast, irrigate);
        calculateSoilMoisture(soilMoistureWest, irrigate);

        if(timeOfYearCounter % (365/4) == 0 && timeOfYearCounter != 0) {
            clock++;
        }
        timeOfYearCounter++;

    }

    private void calculateSoilMoisture(ContextualizedTableEntry initialSoilMoisture, double irrigate) {

        double soilMoistureValue = initialSoilMoisture.getValue();
        double unroundedSoilMoisture = (soilMoistureValue - random(0.0, 0.02)) + (irrigate * 0.02);

        if(unroundedSoilMoisture < 0 ){
             unroundedSoilMoisture = 0;
         }else if(unroundedSoilMoisture > 1){
             unroundedSoilMoisture = 1;
         }

        initialSoilMoisture.setValue(unroundedSoilMoisture);
        initialSoilMoisture.setWhen(getTimeOfYear());
    }

    private TimeOfYear getTimeOfYear() {
        return timeOfYear[clock % timeOfYear.length];
    }

    private void calculateTemperature(){

        double minChange = 0.0;
        double maxChange = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        switch(getTimeOfYear()){
            case SUMMER:
                maxChange = 1.0;
                lowerBound = 16.0;
                upperBound = 30.0;
                break;
            case AUTUMN:
                maxChange = 3.0;
                lowerBound = 3.0;
                upperBound = 14.0;
                break;
            case WINTER:
                maxChange = 2.0;
                lowerBound = -20.0;
                upperBound = 0.0;
                break;
            case SPRING:
                maxChange = 3.0;
                lowerBound = 5.0;
                upperBound = 15.0;
                break;
        }

        temperature.setValue(randomizeSensorValue(temperature.getValue(), minChange, maxChange, lowerBound, upperBound, 0));
        temperature.setWhen(getTimeOfYear());

    }

    private void calculateUvLight(){

        uvLight.setValue(randomizeSensorValue(uvLight.getValue(), 0.0, 1.0, 0.0, 5.0, 0));
        uvLight.setWhen(getTimeOfYear());
    }

    private void calculateWindSpeed(){

        windSpeed.setValue(randomizeSensorValue(windSpeed.getValue(), 0.0, 0.1, 0, 32.7, 1));
        windSpeed.setWhen(getTimeOfYear());
    }

    private double randomizeSensorValue(double value, double minChange, double maxChange, double lowerBound, double upperBound, int decimals) {
        boolean add = random(0, 1) > 0.5;

        if(add) {
            value += random(minChange, maxChange);
        }else{
            value -= random(minChange, maxChange);
        }

        if(value < lowerBound){
            value = lowerBound;
        }else if(value > upperBound){
            value = upperBound;
        }
        return round(value, decimals);
    }

    private double round(double value, int decimals){

        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(decimals, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();

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
