package brain;

import com.google.common.collect.HashBasedTable;
import tableEntry.TableEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 2016-12-14.
 */
public class BrainTable{

    private HashBasedTable <Integer, String, TableEntry> table = HashBasedTable.create();

    public ArrayList<Integer> tableRowContainsValuesInColumns(Map<String, Double> columnsAndValues) {

        ArrayList<Integer> rows = new ArrayList<>();

        for (Integer row : table.rowKeySet()) {

            final boolean[] containsValues = {true};

            columnsAndValues.forEach((columnName, value) -> {

                if (table.get(row, columnName).getValue() != value) {
                    containsValues[0] = false;
                }

            });

            if(containsValues[0]) {
                rows.add(row);
            }

        }
        return rows;
    }

    public ArrayList<Integer> tableRowContainsValueInColumn(String columnName, double value) {

        Map<String, Double> columnAndValue = new HashMap<>();
        columnAndValue.put(columnName, value);

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
                DecimalFormat df = new DecimalFormat("#.##");
                double value = table.get(row, column).getValue();

                output += "\t" + column + ": " + df.format(value) + separator;
            }
            output += "\n";
        }
        return output;

    }
}
