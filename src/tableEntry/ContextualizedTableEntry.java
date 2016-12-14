package tableEntry;


import java.util.Date;

/**
 * Created by simpa2k on 2016-12-07.
 */
public class ContextualizedTableEntry extends TableEntry {

    private String who;
    private String what;
    private Date when;
    private String where;
    private String which;

    public ContextualizedTableEntry(Double value, String who, String what, Date when, String where, String which) {

        super(value);

    }

    /*  public String getContext() {
        return context;
    } */
}
