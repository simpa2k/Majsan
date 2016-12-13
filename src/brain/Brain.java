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

    /*

    1. Finns nuvarande värde? - Om nej: chansa på action. Annars:
    2. Kolla igenom de rader där nuvarande värde finns.
    3. Jämför resultat med mål.
    4. För varje sån rad, jämför SMA och SMB med mål. Mål-SMB > Mål-SMA -> Bra!

    */


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
        lastAction.setValue((lastAction.getValue() + 0.2) * sensors.get(SmartAgricultureWorld.SOIL_MOISTURE));

        HashMap<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, lastAction.getValue());

        System.out.println(probTable);
        return actions;
    }

    public int getTimeStep() {

        //Placeholder
        return 0;
    }

    public void visualize() {
        System.out.println("Brain not implemented");
    }

}
