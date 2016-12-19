package brain;

import tableEntry.ContextualizedTableEntry;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Brain {

    private String name;

    private Map<String, ContextualizedTableEntry> lastSensorValues;
    private TableEntry lastAction;
    private BrainTable probTable = new BrainTable();
    private DecisionMaker decisionMaker;

    private int timestep = 0;
    private int numberOfRows = 0;

    private double soilMoistureGoal = 0.3;

    public Brain(String name, Map<String, ContextualizedTableEntry> lastSensorValues) {

        this.name = name;
        this.lastAction =  new TableEntry(0.0);
        this.lastSensorValues = new HashMap<>(lastSensorValues);
        this.decisionMaker = new DecisionMaker(probTable, soilMoistureGoal);

    }

    public Map<String, Double> senseActLearn(Map<String, ContextualizedTableEntry> sensors, double reward) {

        timestep += 1;

        Map<String, TableEntry> columnsAndValues = new HashMap<>();

        lastSensorValues.forEach((sensorID, value) -> {
            String columnName = sensorID;
            if(sensorID.equals("Soil Moisture")){
                columnName += ", before";
                columnName = columnName.toLowerCase();
            }
            columnsAndValues.put(columnName, value);
        });

        columnsAndValues.put("action", lastAction);

        ArrayList<Integer> rows = probTable.tableRowContainsValuesInColumns(columnsAndValues);

        TableEntry currentOpportunityCount = null;

        boolean appendNewRow = true;
        if (!rows.isEmpty()) {

            for (Integer row : rows) {

                currentOpportunityCount = probTable.get(row, "opportunities");
                currentOpportunityCount.increment();

                TableEntry currentObservationCount = probTable.get(row, "observations");

               if (sensors.get(SmartAgricultureWorld.SOIL_MOISTURE).getValue() == probTable.get(row, "soil moisture, after").getValue()) {
                    currentObservationCount.increment();
                    appendNewRow = false;

                }

                TableEntry currentProbabilityEntry = probTable.get(row, "probability");
                currentProbabilityEntry.setValue(currentObservationCount.getValue() / currentOpportunityCount.getValue());
            }
        }


        if(appendNewRow) {

            numberOfRows++;

            lastSensorValues.forEach((sensorID, value) -> {
                String columnName = sensorID;
                if(sensorID.equals("Soil Moisture")){
                    columnName += ", before";
                    columnName = columnName.toLowerCase();
                }
                probTable.put(numberOfRows, columnName, value);
            });
            probTable.put(numberOfRows, "action", new TableEntry(lastAction.getValue()));
            probTable.put(numberOfRows, "soil moisture, after", new ContextualizedTableEntry(sensors.get(SmartAgricultureWorld.SOIL_MOISTURE).getValue(),
                    sensors.get(SmartAgricultureWorld.SOIL_MOISTURE).getWhen(),
                    sensors.get(SmartAgricultureWorld.SOIL_MOISTURE).getWhich()));

            double opportunities = currentOpportunityCount == null ? 1 : currentOpportunityCount.getValue();
            double observations = 1;
            double probability = observations/opportunities;

            probTable.put(numberOfRows, "opportunities", new TableEntry((opportunities)));
            probTable.put(numberOfRows, "observations", new TableEntry(observations));
            probTable.put(numberOfRows, "probability", new TableEntry(probability));
            //probTable.put(numberOfRows, "reward", new TableEntry(reward));
        }

       // ContextualizedTableEntry oldEntry = sensors.get(SmartAgricultureWorld.SOIL_MOISTURE);
       // ContextualizedTableEntry newEntry = new ContextualizedTableEntry(oldEntry.getValue(), oldEntry.getWhen(), oldEntry.getWhich());

        sensors.forEach((sensorID, value) -> {

            String columnName = sensorID;
            if(sensorID.equals("Soil Moisture")){
                columnName += ", before";
                columnName = columnName.toLowerCase();
            }

            ContextualizedTableEntry newEntry = new ContextualizedTableEntry(value.getValue(), value.getWhen(), value.getWhich());
            lastSensorValues.put(columnName, newEntry);
        });

        double action = decisionMaker.makeDecision(sensors);
        lastAction.setValue(action);

        HashMap<String, Double> actions = new HashMap<>();
        actions.put(SmartAgricultureWorld.IRRIGATE, action);

        System.out.println(probTable.visualizeTable(true));

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
