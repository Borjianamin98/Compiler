package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.descriptor.hastype.FieldDSCP;

import java.util.List;

public class RecordTypeDSCP extends TypeDSCP {
    private List<FieldDSCP> fields;

    public RecordTypeDSCP(String name, int size, List<FieldDSCP> fields) {
        super(name, size, false);
        this.fields = fields;
    }

    public List<FieldDSCP> getFields() {
        return fields;
    }

    public boolean containsField(String name) {
        for (FieldDSCP field : fields) {
            if (field.getName().equals(name))
                return true;
        }
        return false;
    }

    public FieldDSCP getField(String name) {
        for (FieldDSCP field : fields) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }
}
