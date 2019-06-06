package semantic.symbolTable.descriptor;

import semantic.syntaxTree.declaration.record.Field;

import java.util.List;

public class RecordTypeDSCP extends TypeDSCP {
    private List<Field> fields;

    public RecordTypeDSCP(String name, int size, List<Field> fields) {
        super(name, size, false);
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }
}
