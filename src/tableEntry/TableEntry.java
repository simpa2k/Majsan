package tableEntry;

public class TableEntry {

    protected double value;

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

    public String toString(){
        return "" + value;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof  TableEntry) || obj == null) {
            return false;
        }

        TableEntry other = (TableEntry) obj;

        return (other.value == this.value);
    }

}