package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class FieldDSCP extends HasTypeDSCP {
    private String owner;

    public FieldDSCP(String owner, String name, TypeDSCP type, boolean constant, boolean initialized) {
        super(name, type, constant, initialized);
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}
