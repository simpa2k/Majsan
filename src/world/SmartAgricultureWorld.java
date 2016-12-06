package world;

import java.util.Map;

import sensorPark.SensorPark;

public class SmartAgricultureWorld extends World {

    public static final String SOIL_MOISTURE = "Soil Moisture";
    public static final String IRRIGATE = "Irrigate";

    private boolean acted = false;
    private boolean irrigating = false;
    private int lastScan;

    private SensorPark sensorPark;

    public SmartAgricultureWorld(Integer lifespan, String name, int numSensors, int numActions, SensorPark sensorPark) {
        
        super(lifespan, name, numSensors, numActions);
        this.sensorPark = sensorPark;

        scan();

    }

    private void scan() {
        sensors = sensorPark.getSensorValues();
    }

    @Override
    public Step step(Map<String, Double> actions) {

        timestep += 1;

        double soilMoisture = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);
        double irrigate = actions.get(SmartAgricultureWorld.IRRIGATE);

        if (irrigate > 0.5 && !irrigating) {

            irrigating = true;
            acted = true;

        } else if (irrigate < 0.5 && irrigating) {

            irrigating = false;
            acted = true;

        } else {
            acted = false;
        }

        if (soilMoisture < 0.25 && !irrigating) {
            
            reward = -0.9;

        } else if (soilMoisture > 0.25 && irrigating) {
            
            reward = -0.5;
            
        } else {
            reward = 1.0;
        }

        if (timestep - lastScan > 500) {

            scan();
            lastScan = timestep;

        }

        return new Step(sensors, reward);

    }

}
