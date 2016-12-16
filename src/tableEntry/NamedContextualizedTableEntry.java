package tableEntry;

import world.TimeOfYear;

/**
 * Created by Maja on 2016-12-16.
 */
public class NamedContextualizedTableEntry extends ContextualizedTableEntry {

    private String who; //which sensor
    //    private String what; //leave it out for the time being

    public NamedContextualizedTableEntry(Double value, TimeOfYear when, String which, String who){

        super(value, when, which);
        this.who = who;

    }

    public String getWho() {
        return who;
    }
}
