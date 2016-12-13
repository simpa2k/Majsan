package brain;

import com.google.common.collect.HashBasedTable;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;

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
        lastAction =  new TableEntry(0.0);

        lastSoilMoisture = initialSoilMoisture;

    }

    private ArrayList<Integer> tableRowContainsBoth(Double lastSoilMoistureValue, Double lastActionValue) {

        ArrayList<Integer> rows = new ArrayList<>();
        for (Integer row : probTable.rowKeySet()) {

            boolean containsLastSoilMoistureValue = false;
            boolean containsLastActionValue = false;

            for (String column : probTable.columnKeySet()) {

                if (probTable.get(row, "soil moisture, before").getValue() == lastSoilMoistureValue) {
                    containsLastSoilMoistureValue = true;
                }

                if (probTable.get(row, "action").getValue() == lastActionValue) {
                    containsLastActionValue = true;
                }
            }

            if(containsLastSoilMoistureValue && containsLastActionValue) {
                rows.add(row);
            }

        }
        return rows;
    }

    private ArrayList<Integer> tableRowContainsValueInColumn(Double value, String columnName) {

        ArrayList<Integer> rows = new ArrayList<>();
        for (Integer row : probTable.rowKeySet()) {

            boolean containsValue = false;

            if (probTable.get(row, columnName).getValue() == value) {
                containsValue = true;
            }

            if(containsValue) {
                rows.add(row);
            }

        }
        return rows;
    }

    /*

    1. Finns nuvarande värde? - Om nej: chansa på action. Annars:
    2. Kolla igenom de rader där nuvarande värde finns.
    3. För varje sån rad, jämför SMA och SMB med mål. Mål-SMB > Mål-SMA -> Bra!
            -Om det finns rader där vi kom närmare målet, välj samma handling som tar oss närmast målet.
            -Om det inte finns rader där vi kom närmare målet, tar vi vilken som helst av dom som vi fick ut och gör motsatt handling.
             Det spelar ingen roll vilken av dem som är närmst målet.

    */

    private double makeDecision(Map<String, Double> sensors) {

        double action;
        double smb = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);

        ArrayList<Integer> rows = tableRowContainsValueInColumn(smb, "soil moisture, before");

        if (!rows.isEmpty()) {

            Integer rowBestResult = null;
            double bestDiffGoalSMA = 0.0;

            for (Integer row : rows) {
                double diffGoalSMB = soilMoistureGoal - smb;
                double diffGoalSMA = soilMoistureGoal - probTable.get(row, "soil moisture, after").getValue();

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
                action = probTable.get(rows.get(0), "action").getValue() == 0 ? 1 : 0;
            }
        }else{
            action = Math.random() > 0.5 ? 1 : 0;
        }
        return action;
    }


    public Map<String, Double> senseActLearn(Map<String, Double> sensors, double reward) {

        timestep += 1;

        ArrayList<Integer> rows = tableRowContainsBoth(lastSoilMoisture.getValue(), lastAction.getValue());
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
            probTable.put(numberOfRows, "action", lastAction);
            probTable.put(numberOfRows, "soil moisture, after", newSoilMoisture);

            double opportunities = currentOpportunityCount == null ? 1 : currentOpportunityCount.getValue();
            double observations = 1;
            double probability = observations/opportunities;

            probTable.put(numberOfRows, "opportunities", new TableEntry((opportunities)));
            probTable.put(numberOfRows, "observations", new TableEntry(observations));
            probTable.put(numberOfRows, "probability", new TableEntry(probability));
            probTable.put(numberOfRows, "reward", new TableEntry(reward));
        }

        lastSoilMoisture = newSoilMoisture;
        double action = makeDecision(sensors);
        lastAction.setValue(action);

        HashMap<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, action);

        return actions;
    }

    public int getTimeStep() {

        //Placeholder
        return 0;
    }

    public void printProbTable(boolean oneline) {

        String separator = "\n";

        if(oneline) {
            separator = " ";
        }

        for (Integer row : probTable.rowKeySet()) {

            System.out.println("Row: " + row);

            for(String column : probTable.columnKeySet()) {
                System.out.print("\t" + column + ": " + probTable.get(row, column) + separator);
            }
            System.out.print("\n");
        }
        
    }

    public void visualize() {
        System.out.println("Brain not implemented");
    }

}
