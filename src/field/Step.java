package field;

import tableEntry.ContextualizedTableEntry;

import java.util.Map;

public class Step {

    private Map<String, ContextualizedTableEntry> sensors;
    private double reward;

    public Step(Map<String, ContextualizedTableEntry> sensors, double reward) {

        this.sensors = sensors;
        this.reward = reward;
        
    }

    public Map<String, ContextualizedTableEntry> getSensors() {
        return sensors;
    }

    public double getReward() {
        return reward;
    }
}
