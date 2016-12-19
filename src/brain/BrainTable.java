package brain;

import com.google.common.collect.HashBasedTable;
import tableEntry.TableEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 2016-12-14.
 */
public class BrainTable{

    private HashBasedTable <Integer, String, TableEntry> table = HashBasedTable.create();

    public ArrayList<Integer> tableRowContainsValuesInColumns(Map<String, TableEntry> columnsAndValues) {

        ArrayList<Integer> rows = new ArrayList<>();

        for (Integer row : table.rowKeySet()) {

            final boolean[] containsValues = {true};

            columnsAndValues.forEach((columnName, tableEntry) -> {
                if (!(table.get(row, columnName).equals(tableEntry))) {
                    containsValues[0] = false;
                    // Check if the loop can be broken here
                }

            });

            if(containsValues[0]) {
                rows.add(row);
            }

        }
        return rows;
    }

    public ArrayList<Integer> tableRowContainsValueInColumn(String columnName, TableEntry tableEntry) {

        Map<String, TableEntry> columnAndValue = new HashMap<>();
        columnAndValue.put(columnName, tableEntry);

        return tableRowContainsValuesInColumns(columnAndValue);

    }

    public TableEntry get(Integer rowKey, String columnKey) {

        return table.get(rowKey, columnKey);

    }

    public void put(Integer rowKey, String columnKey, TableEntry tableEntry) {

        table.put(rowKey, columnKey, tableEntry);
    }

    public String visualizeTable(boolean oneline) {

        String output = "";
        String separator = "\n";

        if(oneline) {
            separator = " ";
        }

        for (Integer row : table.rowKeySet()) {

            output += "Row: " + row;

            for(String column : table.columnKeySet()) {
                TableEntry value = table.get(row, column);

                output += "\t" + column + ": " + value.toString() + separator;
            }
            output += "\n";
        }
        return output;

    }

    public String dump(double goal){

        String output = "";

        for(Integer row : table.rowKeySet()){

            for(String column : table.columnKeySet()){
                TableEntry value = table.get(row, column);

                output += value.getValue() + ";";
            }
            double smb = table.get(row, "soil moisture, before").getValue();
            double sma = table.get(row, "soil moisture, after").getValue();

            double diffGoalSmb = Math.abs(goal - smb);
            double diffGoalSma = Math.abs(goal - sma);

            if(diffGoalSma < diffGoalSmb){
                output += 1;

            }else{
                output += 0;
            }

            output += ";\n";
        }

        return output;
    }
}
