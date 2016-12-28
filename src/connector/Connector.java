package connector;

import brain.Brain;
import field.SoilMoistureDependentField;
import field.Step;
import field.Field;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Connector {

    private String dumpFileName = String.format("dumps/dump_%s.txt", LocalDateTime.now());

    public void run(Field field) {

        double soilMoistureAverage = 0;

        Map<String, Double> actions = new HashMap<>();
        actions.put(SoilMoistureDependentField.IRRIGATE, 0.0);

        Step step = field.step(actions);
        soilMoistureAverage = step.getSensors().get(SoilMoistureDependentField.SOIL_MOISTURE).getValue();

        Brain brain = new Brain("Smart Agriculture Brain", step.getSensors());

        while(field.isAlive()) {

            actions = brain.senseActLearn(step.getSensors(), step.getReward());
            step = field.step(actions);
            soilMoistureAverage += step.getSensors().get(SoilMoistureDependentField.SOIL_MOISTURE).getValue();

            String dump = field.visualize(brain);
            if(dump != null){
                dumpToFile(dump + (soilMoistureAverage/50));
                soilMoistureAverage = 0;
            }
        }
    }

    public void dumpToFile(String dump){

        try(BufferedWriter bw = new BufferedWriter((new FileWriter(dumpFileName, true)))){

            bw.write(dump + "\n");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
