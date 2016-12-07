package tableEntry;

/**
 * Created by simpa2k on 2016-12-07.
 */
public class TableEntry {

    private double value;

    public TableEntry(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    @Override
    public String toString(){
        return "" + value;
    }
}
