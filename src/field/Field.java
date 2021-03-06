package field;

import brain.Brain;
import tableEntry.ContextualizedTableEntry;

import java.util.HashMap;
import java.util.Map;

public abstract class Field {

    private int lifespan;
    private String name;
    protected int timestep;

    private int brainVisualizePeriod = 50;

    protected Map<String, ContextualizedTableEntry> sensors = new HashMap<>();
    protected Map<String, Double> actions = new HashMap<>();

    protected double reward;

    public Field(Integer lifespan, String name) {
        
        if (lifespan == null) {
            this.lifespan = (int) Math.pow(10, 5);
        } else {
            this.lifespan = lifespan;
        }

        this.name = name;
        this.timestep = -1;

        reward = 0;

    }

    /*public Step step(Map<String, Double> actions) {
        
        timestep += 1;
        sensors = new HashMap<String, ContextualizedTableEntry>();
        reward = 0;

        return new Step(sensors, reward);

    }*/
    public abstract Step step(Map<String, Double> actions);

    public boolean isAlive() {
        return timestep < lifespan;
    }

    public String visualize(Brain brain) {

        if (timestep % brainVisualizePeriod == 0) {
            return timestep + "; ";

        }else{
            return null;
        }

    }

    private void visualizeField(Brain brain) {
        
        System.out.format("%s is %s time steps old", name, timestep);
        System.out.format("The brain is %s time steps old", brain.getTimeStep());

    }

    public int getTimestep(){
        return timestep;
    }

    public abstract Map<String, ContextualizedTableEntry> getSensors();
}


