package main;

import connector.Connector;

import sensorPark.SensorPark;

import world.SmartAgricultureWorld;

public class Main {

    public static void main(String[] args) {

        SensorPark sensorPark = new SensorPark();

        SmartAgricultureWorld world = new SmartAgricultureWorld(
                null, 
                "Smart Agriculture World", 
                sensorPark.getNumberOfSensors(), 
                1, 
                sensorPark);

        Connector connector = new Connector();

        connector.run(world);
        
    }
}
