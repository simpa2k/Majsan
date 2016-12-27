package main;

import communicators.ActuatorPark;
import communicators.SensorPark;
import connector.Connector;
import world.Environment;
import world.SoilMoistureDependentField;
import world.TimeOfYear;

public class Main {

    public static void main(String[] args) {

        Environment environment = new Environment(0.4, 20.0, 2.0, 2.4, TimeOfYear.SUMMER);
        ActuatorPark actuatorPark = new ActuatorPark(environment);
        SensorPark sensorPark = new SensorPark(environment);

        SoilMoistureDependentField world = new SoilMoistureDependentField(
                50000,
                "Smart Agriculture Field",
                sensorPark.getNumberOfSensors(),
                1,
                sensorPark,
                actuatorPark);

        Connector connector = new Connector();

        connector.run(world);
    }
}
