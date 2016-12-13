package communicators;

import world.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorPark {
    
    private int numberOfSensors = 1;
    private Map<String, Double> sensorValues = new HashMap<>();
    private Environment environment;


    public SensorPark(Environment environment) {

        this.environment = environment;
        retrieveDataFromSensors();

    }

    private void retrieveDataFromSensors() {

        double soilMoisture = environment.getSoilMoisture();
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
