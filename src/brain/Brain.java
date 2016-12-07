package brain;

import com.google.common.collect.HashBasedTable;
import tableEntry.TableEntry;
import world.SmartAgricultureWorld;

import java.util.HashMap;
import java.util.Map;

public class Brain {

    private String name;

    private HashMap<String, Double> actions = new HashMap<>();
    private HashBasedTable<Integer, String, TableEntry> probTable = HashBasedTable.create();

    private int timestep = 0;

    public Brain(String name, TableEntry<Double> initialSoilMoisture) {

        this.name = name;
        actions.put(SmartAgricultureWorld.IRRIGATE, 0.0);

        probTable.put(timestep, "soil moisture, before", initialSoilMoisture);
        probTable.put(timestep, "action", new TableEntry<>(actions.get(SmartAgricultureWorld.IRRIGATE)));

    }

    private boolean searchTable(TableEntry value) {

    }

    public Map<String, Double> senseActLearn(Map<String, Double> sensors, double reward) {

        timestep += 1;

        probTable.put(timestep, "soil moisture, after", new TableEntry(sensors.get(SmartAgricultureWorld.SOIL_MOISTURE)));

        /*
        Om det finns någon rad med samma soil moisture, before och action:
            öka opportunities

            Om resultat också är samma:
                Öka observation
                bryt

         Lägg in på ny rad
         */

        if ()

        probTable.put("Opportunities", new TableEntry(1.0));
        probTable.put("Observations", new TableEntry(1.0));


        //probTable.put("Probability", new TableEntry())


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
