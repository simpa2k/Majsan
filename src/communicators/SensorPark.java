package communicators;

import tableEntry.ContextualizedTableEntry;
import world.Environment;

import java.util.HashMap;
import java.util.Map;

public class SensorPark {
    
    private int numberOfSensors = 1;
    private Map<String, ContextualizedTableEntry> sensorValues = new HashMap<>();
    private Environment environment;


    public SensorPark(Environment environment) {

        this.environment = environment;
        retrieveDataFromSensors();

    }

    private void retrieveDataFromSensors() {

        ContextualizedTableEntry soilMoisture = environment.getSoilMoisture();
        sensorValues.put("Soil Moisture", soilMoisture);

    }

    public Map<String, ContextualizedTableEntry> getSensorValues() {

        retrieveDataFromSensors();
        return sensorValues;

    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
