package connector;

import brain.Brain;

import world.World;
import world.Step;

public class Connector {

    public void run(World world) {

        Brain brain = new Brain();

        double[] actions = new double[2];
        Step step = world.step(actions);

        while(world.isAlive()) {

            actions = brain.senseActLearn(step.getSensors(), step.getReward());
            step = world.step(actions);

        }
        
    }
}
