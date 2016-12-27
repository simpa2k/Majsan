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

    /**
     * Method to make a decision based on earlier experiences. The decision process is as follows:
     *
     *      If the provided sensor value does not exist in the table -> make a random guess as to what to do.
     *      Else ->
     *
     *          Iterate over the rows where the value is present. For each row ->
     *
     *              If there are one or more rows that took us closer to the goal ->
     *
     *                  Record the index of the row with the best result and how close it got to the goal.
     *
     *          If a row with a good result was found -> perform the same action as recorded on that row.
     *          Else -> pick a row at random from the retrieved ones and perform the opposite action.
     *
     * @param sensors A sensor value type mapped to a sensor value, e.g. "Soil Moisture" -> 0.25.
     * @return action A value of either 0 or 1 representing an action.
     */

    protected double makeDecision(Map<String, ContextualizedTableEntry> sensors) {

        double action;
        TableEntry smb = sensors.get(SoilMoistureDependentField.SOIL_MOISTURE);
        double smbValue = smb.getValue();

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

            Integer rowBestResult = null;
            double bestDiffGoalSMA = 0.0;

            for (Integer row : rows) {
                double diffGoalSMB = Math.abs(soilMoistureGoal - smbValue);
                double diffGoalSMA = Math.abs(soilMoistureGoal - probTable.get(row, "soil moisture, after").getValue());

                if (diffGoalSMA < diffGoalSMB) {

                    if (rowBestResult == null || diffGoalSMA < bestDiffGoalSMA){

                        rowBestResult = row;
                        bestDiffGoalSMA = diffGoalSMA;

                    }else if(diffGoalSMA == bestDiffGoalSMA){

                        double currentRowProbability = probTable.get(row, "probability").getValue();
                        double bestRowProbability = probTable.get(rowBestResult, "probability").getValue();

                        if(currentRowProbability > bestRowProbability){

                            rowBestResult = row;
                            bestDiffGoalSMA = diffGoalSMA;
                        }
                    }
                }
            }
            if (rowBestResult != null) {
                action = probTable.get(rowBestResult, "action").getValue();
            } else {
                int random = (int) Random.random(0, rows.size()-0.1);
                action = probTable.get(rows.get(random), "action").getValue() == 0 ? 1 : 0;
            }
        }else{

            action = Math.random() > 0.5 ? 1 : 0;
            /* ToDo: Might be an idea to loop over the entire table and check which action
             * that has most often resulted in a reward and pick that action.
             * More probability oriented solution than the current one.
             */

        }
        return action;
    }

}
