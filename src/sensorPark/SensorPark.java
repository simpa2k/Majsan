package sensorPark;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorPark {
    
    private int numberOfSensors = 1;
    private Map<String, Double> sensorValues = new HashMap<>();

    public SensorPark() {
        retrieveDataFromSensors();
    }

    private void retrieveDataFromSensors() {

        Random random = new Random();

        double soilMoisture = random.nextDouble() * 0.35;
        sensorValues.put("Soil Moisture", soilMoisture);

    }

    public Map<String, Double> getSensorValues() {
        return sensorValues;
    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
