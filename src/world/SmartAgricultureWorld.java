package world;

import communicators.ActuatorPark;
import communicators.SensorPark;
import tableEntry.ContextualizedTableEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SmartAgricultureWorld extends World {

    public static final String SOIL_MOISTURE = "Soil Moisture";
    public static final String SOIL_MOISTURE_EAST = "Soil Moisture EAST";
    public static final String SOIL_MOISTURE_WEST = "Soil Moisture WEST";
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
        //ContextualizedTableEntry soilMoisture = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);

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
            TimeUnit.MILLISECONDS.sleep(100);
        }catch(InterruptedException exception){

        }
        scan();
        sensors.put(SmartAgricultureWorld.SOIL_MOISTURE, calculateSensorAverage());

        return new Step(sensors, reward);

    }

    public ContextualizedTableEntry calculateSensorAverage(){

        final double[] averageSoilMoisture = {0};
        final TimeOfYear[] currentTimeOfYear = {null};

        sensors.forEach((sensorID, value) -> {

            if(sensorID.equals("Soil Moisture EAST") || sensorID.equals("Soul Moisture WEST")){
                if(currentTimeOfYear[0] == null){
                    currentTimeOfYear[0] = value.getWhen();
                }
                averageSoilMoisture[0] += value.getValue();
            }
        });

        int numberOfSoilMoistureSensors = 2;
        averageSoilMoisture[0] /= numberOfSoilMoistureSensors;

        BigDecimal bigDecimalSoilMoisture = new BigDecimal(averageSoilMoisture[0]);
        bigDecimalSoilMoisture = bigDecimalSoilMoisture.setScale(2, RoundingMode.HALF_UP);

        ContextualizedTableEntry averageEntry = new ContextualizedTableEntry(bigDecimalSoilMoisture.doubleValue(), currentTimeOfYear[0], "Irrigate");

        sensors.remove("Soil Moisture EAST");
        sensors.remove("Soil Moisture WEST");

        return averageEntry;
    }

    public Map<String, ContextualizedTableEntry> getSensors(){
        sensors.put(SmartAgricultureWorld.SOIL_MOISTURE, calculateSensorAverage());
        return sensors;
    }
}
