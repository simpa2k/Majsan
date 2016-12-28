package field;

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

    private double soilMoistureGoal = 0.3;

    private SensorPark sensorPark;
    private ActuatorPark actuatorPark;

    public SoilMoistureDependentField(Integer lifespan,
                                      String name,
                                      SensorPark sensorPark,
                                      ActuatorPark actuatorPark) {
        
        super(lifespan, name);
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
        scan();

        double previousSoilMoisture;
        ContextualizedTableEntry soilMoistureAverage = calculateSensorAverage();

        if (sensors.get(SOIL_MOISTURE) != null) {
            previousSoilMoisture = sensors.get(SOIL_MOISTURE).getValue();
        } else {
            previousSoilMoisture = soilMoistureAverage.getValue();
        }

        sensors.put(SOIL_MOISTURE, soilMoistureAverage);
        reward = calculateReward(previousSoilMoisture, soilMoistureAverage.getValue());

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

    private double calculateReward(double previousSoilMoisture, double currentSoilMoisture) {

        double differenceFromGoalBefore = Math.abs(soilMoistureGoal - previousSoilMoisture);
        double differenceFromGoalNow = Math.abs(soilMoistureGoal - currentSoilMoisture);

        return differenceFromGoalBefore - differenceFromGoalNow;

    }

    public Map<String, ContextualizedTableEntry> getSensors(){

        sensors.put(SoilMoistureDependentField.SOIL_MOISTURE, calculateSensorAverage());
        return sensors;

    }
}
