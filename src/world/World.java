package world;

import java.util.HashMap;
import java.util.Map;

public class World {

    private int lifespan;
    private String name;
    protected int timestep;
    private int numSensors;
    private int numActions;

    protected Map<String, Double> sensors = new HashMap<>();
    protected Map<String, Double> actions = new HashMap<>();

    protected double reward;

    public World(Integer lifespan, String name, int numSensors, int numActions) {
        
        if (lifespan == null) {
            this.lifespan = (int) Math.pow(10, 5);
        } else {
            this.lifespan = lifespan;
        }

        this.name = name;
        this.timestep = -1;
        this.numSensors = numSensors;
        this.numActions = numActions;

        reward = 0;

    }

    public Step step(Map<String, Double> actions) {
        
        timestep += 1;
        sensors = new HashMap<String, Double>();
        reward = 0;

        return new Step(sensors, reward);

    }

    public boolean isAlive() {
        return timestep < lifespan;
    }

}
