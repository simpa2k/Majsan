package main;

import communicators.ActuatorPark;
import communicators.SensorPark;
import connector.Connector;
import field.Environment;
import field.SoilMoistureDependentField;
import field.TimeOfYear;

public class Main {

    public static void main(String[] args) {

        Environment environment = new Environment(0.4, 20.0, 2.0, 2.4, TimeOfYear.SUMMER);
        ActuatorPark actuatorPark = new ActuatorPark(environment);
        SensorPark sensorPark = new SensorPark(environment);

        SoilMoistureDependentField field = new SoilMoistureDependentField(
                10000,
                "Smart Agriculture Field",
                sensorPark,
                actuatorPark);

        Connector connector = new Connector();

        connector.run(field);
    }
}
