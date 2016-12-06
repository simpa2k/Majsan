package world;

import java.util.Map;

public class Step {

    private Map<String, Double> sensors;
    private double reward;

    public Step(Map<String, Double> sensors, double reward) {

        this.sensors = sensors;
        this.reward = reward;
        
    }

    public Map<String, Double> getSensors() {
        return sensors;
    }

    public double getReward() {
        return reward;
    }
}
