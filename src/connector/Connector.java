package connector;

import brain.Brain;
import tableEntry.ContextualizedTableEntry;
import world.SmartAgricultureWorld;
import world.Step;
import world.World;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Connector {

    public void run(World world) {

        double soilMoistureAverage = 0;

        Map<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, 0.0);

        Step step = world.step(actions);
        soilMoistureAverage = step.getSensors().get(SmartAgricultureWorld.SOIL_MOISTURE).getValue();


        Brain brain = new Brain("Smart Agriculture Brain", step.getSensors());

        while(world.isAlive()) {

            actions = brain.senseActLearn(step.getSensors(), step.getReward());
            step = world.step(actions);
            soilMoistureAverage += step.getSensors().get(SmartAgricultureWorld.SOIL_MOISTURE).getValue();

            String dump = world.visualize(brain);
            if(dump != null){
                dumpToFile(dump + (soilMoistureAverage/50));
                soilMoistureAverage = 0;
            }
        }
    }

    public void dumpToFile(String dump){

        try(BufferedWriter bw = new BufferedWriter((new FileWriter("Dump.txt", true)))){

            bw.write(dump + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
