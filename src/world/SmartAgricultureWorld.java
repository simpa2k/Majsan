package world;

import communicators.ActuatorPark;
import communicators.SensorPark;
import tableEntry.ContextualizedTableEntry;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SmartAgricultureWorld extends World {

    public static final String SOIL_MOISTURE = "Soil Moisture";
    public static final String IRRIGATE = "Irrigate";

   /* private boolean acted = false;
    private boolean irrigating = false;
    private int lastScan; */

    private SensorPark sensorPark;
    private ActuatorPark actuatorPark;

    public SmartAgricultureWorld(Integer lifespan, String name, int numSensors, int numActions, SensorPark sensorPark, ActuatorPark actuatorPark) {
        
        super(lifespan, name, numSensors, numActions);
        this.sensorPark = sensorPark;
        this.actuatorPark = actuatorPark;

        scan();

    }

    private void scan() {
        sensors = sensorPark.getSensorValues();
    }

    @Override
    public Step step(Map<String, Double> actions) {

        timestep += 1;

        actuatorPark.actuate(actions);

        double irrigate = actions.get(SmartAgricultureWorld.IRRIGATE);
        ContextualizedTableEntry soilMoisture = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);

      /*  if (irrigate > 0.5 && !irrigating) {

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
        }*/

        System.out.println("Sleep");
        try{
            TimeUnit.MILLISECONDS.sleep(1000);
        }catch(InterruptedException exception){

        }
        scan();

        return new Step(sensors, reward);

    }

}
