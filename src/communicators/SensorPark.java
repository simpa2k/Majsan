package communicators;

import tableEntry.ContextualizedTableEntry;
import world.Environment;

import java.util.HashMap;
import java.util.Map;

public class SensorPark {
    
    private int numberOfSensors = 2;
    private Map<String, ContextualizedTableEntry> sensorValues = new HashMap<>();
    private Environment environment;

    private String soilMoistureWestID = "Soil Moisture WEST";
    private String soilMoistureEastID = "Soil Moisture EAST";

    public SensorPark(Environment environment) {

        this.environment = environment;
        retrieveDataFromSensors();

    }

    private void retrieveDataFromSensors() {

        ContextualizedTableEntry soilMoistureWest = environment.getSoilMoisture(soilMoistureWestID);
        sensorValues.put(soilMoistureWestID, soilMoistureWest);

        ContextualizedTableEntry soilMoistureEast = environment.getSoilMoisture(soilMoistureEastID);
        sensorValues.put(soilMoistureEastID, soilMoistureEast);

    }

    public Map<String, ContextualizedTableEntry> getSensorValues() {

        retrieveDataFromSensors();
        return sensorValues;

    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }
}
