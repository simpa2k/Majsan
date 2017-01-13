package tableEntry;

import field.TimeOfYear;

public class NamedContextualizedTableEntry extends ContextualizedTableEntry {

    private String who; //which sensor

    public NamedContextualizedTableEntry(Double value, TimeOfYear when, String which, String who){

        super(value, when, which);
        this.who = who;

    }

    public String getWho() {
        return who;
    }
}
