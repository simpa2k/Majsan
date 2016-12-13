package sensorPark;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorPark {
    
    private int numberOfSensors = 1;
    private Map<String, Double> sensorValues = new HashMap<>();
    private Random random;

    public SensorPark() {

        random = new Random();
        retrieveDataFromSensors();

    }

    private void retrieveDataFromSensors() {

        double soilMoisture = random(0.2, 0.33);
        sensorValues.put("Soil Moisture", soilMoisture);

    }

    private double random(double min, double max){

        return Math.random() * (max-min) + min;

    }

    public Map<String, Double> getSensorValues() {

        retrieveDataFromSensors();
        return sensorValues;

    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
