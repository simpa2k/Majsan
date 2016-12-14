package connector;

import brain.Brain;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;
import world.Step;
import world.World;

import java.util.HashMap;
import java.util.Map;

public class Connector {

    public void run(World world, double initialSoilMoisture) {

        Brain brain = new Brain("Smart Agriculture Brain", new TableEntry(initialSoilMoisture));

        Map<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, 0.0);

        Step step = world.step(actions);

        while(world.isAlive()) {

            actions = brain.senseActLearn(step.getSensors(), step.getReward());
            step = world.step(actions);

        }
    }
}
