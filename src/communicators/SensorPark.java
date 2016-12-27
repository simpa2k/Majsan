package communicators;

import tableEntry.ContextualizedTableEntry;
import field.Environment;

import java.util.HashMap;
import java.util.Map;

public class SensorPark {
    
    private int numberOfSensors = 5;
    private Map<String, ContextualizedTableEntry> sensorValues = new HashMap<>();
    private Environment environment;

    private String [] sensorIDs = new String[] {"Soil Moisture WEST", "Soil Moisture EAST", "Temperature", "UV Light", "Wind Speed"};

    public SensorPark(Environment environment) {

        this.environment = environment;
        retrieveDataFromSensors();

    }

    private void retrieveDataFromSensors() {

        for(int i=0; i<sensorIDs.length; i++){
            putSensorData(sensorIDs[i]);
        }

    }

    private void putSensorData(String ID){

        ContextualizedTableEntry newSensorValue = environment.getSensorLevels(ID);
        sensorValues.put(ID, newSensorValue);
    }

    public Map<String, ContextualizedTableEntry> getSensorValues() {

        retrieveDataFromSensors();
        return sensorValues;

    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
