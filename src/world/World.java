package world;

public class World {

    public Step step(double[] actions) {
        return null;
    }

    public boolean isAlive() {
        return false;
    }

    public class Step {

        double[] sensors;
        double reward;

        public Step(double[] sensors, double reward) {

            this.sensors = sensors;
            this.reward = reward;
            
        }

        public double[] getSensors() {
            return sensors;
        }

        public double getReward() {
            return reward;
        }
    }
}
