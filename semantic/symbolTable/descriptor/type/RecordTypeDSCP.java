package semantic.symbolTable.descriptor.type;

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

    public boolean containsField(String name) {
        for (Field field : fields) {
            if (field.getName().equals(name))
                return true;
        }
        return false;
    }

    public Field getField(String name) {
        for (Field field : fields) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }
}
