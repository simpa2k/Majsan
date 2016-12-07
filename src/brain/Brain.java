package brain;

import com.google.common.collect.HashBasedTable;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Brain {

    private String name;

    private TableEntry lastAction;
    private HashBasedTable<Integer, String, TableEntry> probTable = HashBasedTable.create();

    private int timestep = 0;
    private int numberOfRows = 0;

    public Brain(String name, TableEntry initialSoilMoisture) {

        this.name = name;
        lastAction =  new TableEntry(0.0);

        probTable.put(timestep, "soil moisture, before", initialSoilMoisture);
        probTable.put(timestep, "action", lastAction);

    }

    private ArrayList<Integer> tableRowContainsBoth(Double value1, Double value2) {

        ArrayList<Integer> rows = new ArrayList<>();
        for (Integer row : probTable.rowKeySet()) {

            boolean containsValue1 = false;
            boolean containsValue2 = false;

            for (String column : probTable.columnKeySet()) {

                if (probTable.get(row, column).getValue() == value1) {
                    containsValue1 = true;
                }

                if (probTable.get(row, column).getValue() == value2) {
                    containsValue2 = true;
                }

            }

            if(containsValue1 && containsValue2) {
                rows.add(row);
            }

        }
        return rows;
    }

    public Map<String, Double> senseActLearn(Map<String, Double> sensors, double reward) {

        timestep += 1;


        /*
        Om det finns någon rad med samma soil moisture, before och action:
            öka opportunities

            Om resultat också är samma:
                Öka observation
                bryt

         Lägg in på ny rad
         */

        //TableEntry soilMoistureEntry = probTable.get(numberOfRows, "soil moisture, before");
        //double soilMoistureBefore = soilMoistureEntry.getValue();

        //Integer row = tableRowContainsBoth(soilMoistureBefore, lastAction.getValue());
        ArrayList<Integer> rows = tableRowContainsBoth(lastSoilMoisture, lastAction.getValue());


        boolean appendNewRow = true;
        if (!rows.isEmpty()) {

            for (Integer row : rows) {

                TableEntry currentOpportunityCount = probTable.get(row, "opportunities");
                currentOpportunityCount.increment();

                if (sensors.get(SmartAgricultureWorld.SOIL_MOISTURE) == probTable.get(row, "soil moisture, after").getValue()) {

                    TableEntry currentObservationCount = probTable.get(row, "observations");
                    currentObservationCount.increment();

                    appendNewRow = false;

                }
            }
        }

        if(appendNewRow) {

            numberOfRows++;

            probTable.put(numberOfRows, "soil moisture, before", lastSoilMoisture);
            probTable.put(numberOfRows, "action", lastAction);
            probTable.put(numberOfRows, "soil moisture, after", new TableEntry(sensors.get(SmartAgricultureWorld.SOIL_MOISTURE)));

            double opportunities = 1;
            double observations = 1;
            double probability = observations/opportunities;

            probTable.put(numberOfRows, "opportunities", new TableEntry((opportunities)));
            probTable.put(numberOfRows, "observations", new TableEntry(observations));
            probTable.put(numberOfRows, "probability", new TableEntry(probability));
            probTable.put(numberOfRows, "reward", new TableEntry(reward));

        }

        probTable.put("Observations", new TableEntry(1.0));


        //probTable.put("Probability", new tableEntry())


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
