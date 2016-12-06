package connector;

import java.util.HashMap;
import java.util.Map;

import brain.Brain;

import world.World;
import world.SmartAgricultureWorld;
import world.Step;

public class Connector {

    public void run(World world) {

        Brain brain = new Brain();

        Map<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, 0.0);

        Step step = world.step(actions);

        while(world.isAlive()) {

            actions = brain.senseActLearn(step.getSensors(), step.getReward());
            step = world.step(actions);

        }
        
    }
}
