package tableEntry;


import world.TimeOfYear;


/**
 * Created by simpa2k on 2016-12-07.
 */
public class ContextualizedTableEntry extends TableEntry {

    private TimeOfYear when; //time of year
    private String which; //which actuator, in our case irrigator

    public ContextualizedTableEntry(Double value, TimeOfYear when, String which) {

        super(value);
        this.when = when;
        this.which = which;

    }

     public TimeOfYear getWhen() {
        return when;
    }

    public String getWhich() {
        return which;
    }

    public void setWhen(TimeOfYear when) {
        this.when = when;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof  ContextualizedTableEntry) || obj == null) {
            return false;
        }

        ContextualizedTableEntry other = (ContextualizedTableEntry) obj;

        return (super.equals(other)) && (other.when.equals(this.when));
    }

    @Override
    public String toString(){

        return super.toString() + ", " + when + ", " + which;

    }
}

    /*  public String getContext() {
        return context;
    } */

