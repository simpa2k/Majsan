package main;

import communicators.ActuatorPark;
import communicators.SensorPark;
import connector.Connector;
import tableEntry.ContextualizedTableEntry;
import world.Environment;
import world.SmartAgricultureWorld;
import world.TimeOfYear;

public class Main {

    public static void main(String[] args) {


        //ContextualizedTableEntry initialSoilMoisture = new ContextualizedTableEntry(0.15, "SoilMoistureSensor", TimeOfYear.SUMMER, "Irrigator");
        //Environment environment = new Environment(initialSoilMoisture);
        Environment environment = new Environment(0.15, TimeOfYear.SUMMER);
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

        connector.run(world, world.calculateSensorAverage());
    }
}
