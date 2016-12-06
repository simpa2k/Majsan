package brain;

import world.SmartAgricultureWorld;

import java.util.HashMap;
import java.util.Map;

public class Brain {

    public Map<String, Double> senseActLearn(Map<String, Double> sensors, double reward) {

        //Placeholder
        HashMap<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, 0.0);

        return actions;

    }

    public int getTimeStep() {

        //Placeholder
        return 0;
    }

    public void visualize() {
        System.out.println("Brain not implemented");
    }

}
