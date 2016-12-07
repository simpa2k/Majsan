package tableEntry;

/**
 * Created by simpa2k on 2016-12-07.
 */
public class ContextualizedTableEntry extends TableEntry {

    private String context;

    public ContextualizedTableEntry(Double value, String context) {

        super(value);
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
