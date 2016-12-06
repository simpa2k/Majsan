package main;

import connector.Connector;
import world.World;

public class Main {

    public static void main(String[] args) {

        World world = new World();
        Connector connector = new Connector();

        connector.run(world);
        
    }
}
