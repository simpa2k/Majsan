package sensorPark;

import java.util.HashMap;
import java.util.Map;

public class SensorPark {
    
    private int numberOfSensors = 1;
    private Map<String, Double> sensorValues = new HashMap<>();

    public SensorPark() {
        retrieveDataFromSensors();
    }

    private void retrieveDataFromSensors() {
        
        double soilMoisture = 0.3;
        sensorValues.put("Soil Moisture", soilMoisture);

    }

    public Map<String, Double> getSensorValues() {
        return sensorValues;
    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
