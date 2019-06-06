package semantic.symbolTable.descriptor;

import java.util.List;

public class RecordDSCP implements DSCP {
    private String name;
    private List<String> fields;

    public RecordDSCP(String name, List<String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }
}
