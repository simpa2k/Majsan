package brain;

import random.Random;
import tableEntry.ContextualizedTableEntry;
import tableEntry.TableEntry;
import field.SoilMoistureDependentField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 2016-12-14.
 */
public class DecisionMaker {

    private BrainTable probTable;
    private double soilMoistureGoal;

    public DecisionMaker(BrainTable probTable, double soilMoistureGoal) {

        this.probTable = probTable;
        this.soilMoistureGoal = soilMoistureGoal;
    }

    protected double makeDecision(Map<String, ContextualizedTableEntry> sensors) {

        double action;

        Map<String, TableEntry> valuesAndColumns = new HashMap<>();

        sensors.forEach((sensorID, value) -> {
            String columnName = sensorID;
            if(sensorID.equals("Soil Moisture")){
                columnName += ", before";
                columnName = columnName.toLowerCase();
            }
            valuesAndColumns.put(columnName, value);
        });

        ArrayList<Integer> rows = probTable.tableRowContainsValuesInColumns(valuesAndColumns);
        if (!rows.isEmpty()) {

            Integer rowHighestReward = null;
            double highestReward = 0.0;

            for (Integer row : rows) {

                double reward = probTable.get(row, "reward").getValue();

                if (reward > 0) {

                    if (rowHighestReward == null || reward > highestReward){

                        rowHighestReward = row;
                        highestReward = reward;

                    } else if(reward == highestReward) {

                        double currentRowProbability = probTable.get(row, "probability").getValue();
                        double bestRowProbability = probTable.get(rowHighestReward, "probability").getValue();

                        if(currentRowProbability > bestRowProbability){

                            rowHighestReward = row;
                            highestReward = reward;

                        }
                    }
                }
            }

            if (rowHighestReward != null) {

                action = probTable.get(rowHighestReward, "action").getValue();

            } else {

                int random = (int) Random.random(0, rows.size() - 0.1);
                action = probTable.get(rows.get(random), "action").getValue() == 0 ? 1 : 0;

            }

        } else {

            action = Math.random() > 0.5 ? 1 : 0;
            /* ToDo: Might be an idea to loop over the entire table and check which action
             * that has most often resulted in a reward and pick that action.
             * More probability oriented solution than the current one.
             */

        }
        return action;
    }

}
