package world;

public class World {

    private int lifespan;
    private String name;
    private int timestep;
    private int numSensors;
    private int numActions;

    private double[] sensors;
    private double[] actions;

    private double reward;

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

        sensors = new double[numSensors];
        actions = new double[numActions];

        reward = 0;

    }

    public Step step(double[] actions) {
        
        timestep += 1;
        sensors = new double[numSensors]; 
        reward = 0;

        return new Step(sensors, reward);

    }

    public boolean isAlive() {
        return timestep < lifespan;
    }

}
