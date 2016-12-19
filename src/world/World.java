package world;

import brain.Brain;
import tableEntry.ContextualizedTableEntry;

import java.util.HashMap;
import java.util.Map;

public abstract class World {

    private int lifespan;
    private String name;
    protected int timestep;
    private int numSensors;
    private int numActions;

    private int worldVisualizePeriod = 0x1e6;
    private int brainVisualizePeriod = 50;

    protected Map<String, ContextualizedTableEntry> sensors = new HashMap<>();
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
        sensors = new HashMap<String, ContextualizedTableEntry>();
        reward = 0;

        return new Step(sensors, reward);

    }

    public boolean isAlive() {
        return timestep < lifespan;
    }

    public String visualize(Brain brain) {

        if (timestep % brainVisualizePeriod == 0) {
            return "Timestep: " + timestep + "\n";

        }else{
            return null;
        }

    }

    private void visualizeWorld(Brain brain) {
        
        System.out.format("%s is %s time steps old", name, timestep);
        System.out.format("The brain is %s time steps old", brain.getTimeStep());

    }

    public int getTimestep(){
        return timestep;
    }

    public abstract Map<String, ContextualizedTableEntry> getSensors();
}


