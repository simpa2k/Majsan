package brain;

import com.google.common.collect.HashBasedTable;
import random.Random;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Brain {

    private String name;

    private TableEntry lastSoilMoisture;
    private TableEntry lastAction;
    private HashBasedTable<Integer, String, TableEntry> probTable = HashBasedTable.create();

    private int timestep = 0;
    private int numberOfRows = 0;

    private double soilMoistureGoal = 0.3;

    public Brain(String name, TableEntry initialSoilMoisture) {

        this.name = name;
        lastAction =  new TableEntry(0.15);
        lastSoilMoisture = initialSoilMoisture;

    }

    private ArrayList<Integer> tableRowContainsValuesInColumns(Map<String, Double> columnsAndValues) {

        ArrayList<Integer> rows = new ArrayList<>();

        for (Integer row : probTable.rowKeySet()) {

            final boolean[] containsValues = {true};

            columnsAndValues.forEach((columnName, value) -> {

                if (probTable.get(row, columnName).getValue() != value) {
                    containsValues[0] = false;
                }

            });

            if(containsValues[0]) {
                rows.add(row);
            }

        }
        return rows;
    }

    private ArrayList<Integer> tableRowContainsValueInColumn(String columnName, double value) {

        Map<String, Double> columnAndValue = new HashMap<>();
        columnAndValue.put(columnName, value);

        return tableRowContainsValuesInColumns(columnAndValue);

    }

    /*

    1. Finns nuvarande värde? - Om nej: chansa på action. Annars:
    2. Kolla igenom de rader där nuvarande värde finns.
    3. För varje sån rad, jämför SMA och SMB med mål. Mål-SMB > Mål-SMA -> Bra!
            -Om det finns rader där vi kom närmare målet, välj samma handling som tar oss närmast målet.
            -Om det inte finns rader där vi kom närmare målet, tar vi vilken som helst av dom som vi fick ut och gör motsatt handling.
             Det spelar ingen roll vilken av dem som är närmst målet.

    */

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
     * @param sensors A sensor value type mapped to a sensor value, e.g. "Soil Moisture" -> 0.25
     * @return action A value of either 0 or 1 representing an action.
     */

    private double makeDecision(Map<String, Double> sensors) {

        double action;
        double smb = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);

        ArrayList<Integer> rows = tableRowContainsValueInColumn("soil moisture, before", smb);

        if (!rows.isEmpty()) {

            Integer rowBestResult = null;
            double bestDiffGoalSMA = 0.0;

            for (Integer row : rows) {
                double diffGoalSMB = Math.abs(soilMoistureGoal - smb);
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

        }
        return action;
    }


    public Map<String, Double> senseActLearn(Map<String, Double> sensors, double reward) {

        timestep += 1;

        Map<String, Double> columnsAndValues = new HashMap<>();

        columnsAndValues.put("soil moisture, before", lastSoilMoisture.getValue());
        columnsAndValues.put("action", lastAction.getValue());

        ArrayList<Integer> rows = tableRowContainsValuesInColumns(columnsAndValues);

         TableEntry currentOpportunityCount = null;

        boolean appendNewRow = true;
        if (!rows.isEmpty()) {

            for (Integer row : rows) {

                currentOpportunityCount = probTable.get(row, "opportunities");
                currentOpportunityCount.increment();

                TableEntry currentObservationCount = probTable.get(row, "observations");

                if (sensors.get(SmartAgricultureWorld.SOIL_MOISTURE) == probTable.get(row, "soil moisture, after").getValue()) {

                    currentObservationCount.increment();
                    appendNewRow = false;

                }

                TableEntry currentProbabilityEntry = probTable.get(row, "probability");
                currentProbabilityEntry.setValue(currentObservationCount.getValue() / currentOpportunityCount.getValue());
            }
        }

        TableEntry newSoilMoisture = new TableEntry(sensors.get(SmartAgricultureWorld.SOIL_MOISTURE));

        if(appendNewRow) {

            numberOfRows++;

            probTable.put(numberOfRows, "soil moisture, before", lastSoilMoisture);
            probTable.put(numberOfRows, "action", new TableEntry(lastAction.getValue()));
            probTable.put(numberOfRows, "soil moisture, after", newSoilMoisture);

            double opportunities = currentOpportunityCount == null ? 1 : currentOpportunityCount.getValue();
            double observations = 1;
            double probability = observations/opportunities;

            probTable.put(numberOfRows, "opportunities", new TableEntry((opportunities)));
            probTable.put(numberOfRows, "observations", new TableEntry(observations));
            probTable.put(numberOfRows, "probability", new TableEntry(probability));
            //probTable.put(numberOfRows, "reward", new TableEntry(reward));
        }

        lastSoilMoisture = newSoilMoisture;
        double action = makeDecision(sensors);
        lastAction.setValue(action);

        HashMap<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, action);

        System.out.println(visualizeProbTable(true));

        return actions;
    }

    public int getTimeStep() {

        //Placeholder
        return 0;
    }

    public String visualizeProbTable(boolean oneline) {

        String output = "";
        String separator = "\n";

        if(oneline) {
            separator = " ";
        }

        for (Integer row : probTable.rowKeySet()) {

            output += "Row: " + row;

            for(String column : probTable.columnKeySet()) {
                DecimalFormat df = new DecimalFormat("#.##");
                double value = probTable.get(row, column).getValue();

                output += "\t" + column + ": " + df.format(value) + separator;
            }
            output += "\n";
        }
        return output;

    }

    public void visualize() {
        System.out.println("Brain not implemented");
    }

}
