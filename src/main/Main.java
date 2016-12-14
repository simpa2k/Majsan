package main;

import communicators.ActuatorPark;
import connector.Connector;
import communicators.SensorPark;
import world.Environment;
import world.SmartAgricultureWorld;

public class Main {

    public static void main(String[] args) {

        Environment environment = new Environment();
        ActuatorPark actuatorPark = new ActuatorPark(environment);
        SensorPark sensorPark = new SensorPark(environment);

        SmartAgricultureWorld world = new SmartAgricultureWorld(
                10000,
                "Smart Agriculture World", 
                sensorPark.getNumberOfSensors(), 
                1, 
                sensorPark,
                actuatorPark);

        Connector connector = new Connector();

        connector.run(world);

    }
}
