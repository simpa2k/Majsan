package world;

import communicators.ActuatorPark;
import communicators.SensorPark;
import tableEntry.ContextualizedTableEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class SoilMoistureDependentField extends Field {

    public static final String SOIL_MOISTURE = "Soil Moisture";
    public static final String SOIL_MOISTURE_EAST = "Soil Moisture EAST";
    public static final String SOIL_MOISTURE_WEST = "Soil Moisture WEST";
    public static final String IRRIGATE = "Irrigate";

    private SensorPark sensorPark;
    private ActuatorPark actuatorPark;

    public SoilMoistureDependentField(Integer lifespan,
                                      String name,
                                      int numSensors,
                                      int numActions,
                                      SensorPark sensorPark,
                                      ActuatorPark actuatorPark) {
        
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

       /*System.out.println("Sleep");
        try{
            TimeUnit.MILLISECONDS.sleep(100);
        }catch(InterruptedException exception){

        }*/
        scan();
        sensors.put(SoilMoistureDependentField.SOIL_MOISTURE, calculateSensorAverage());

        return new Step(sensors, reward);

    }

    public ContextualizedTableEntry calculateSensorAverage(){

        final double[] averageSoilMoisture = {0};
        final TimeOfYear[] currentTimeOfYear = {null};

        sensors.forEach((sensorID, value) -> {

            if(sensorID.equals(SOIL_MOISTURE_EAST) || sensorID.equals(SOIL_MOISTURE_WEST)){
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

        sensors.remove(SOIL_MOISTURE_EAST);
        sensors.remove(SOIL_MOISTURE_WEST);

        return averageEntry;
    }

    public Map<String, ContextualizedTableEntry> getSensors(){
        sensors.put(SoilMoistureDependentField.SOIL_MOISTURE, calculateSensorAverage());
        return sensors;
    }
}
