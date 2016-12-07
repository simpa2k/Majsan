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

        double soilMoisture = random.nextInt(3) * 0.35;
        sensorValues.put("Soil Moisture", soilMoisture);

    }

    public Map<String, Double> getSensorValues() {

        retrieveDataFromSensors();
        return sensorValues;

    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
