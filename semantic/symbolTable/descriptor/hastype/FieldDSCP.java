package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class FieldDSCP extends HasTypeDSCP {
    public FieldDSCP(String name, TypeDSCP type, boolean constant, boolean initialized) {
        super(name, type, constant, initialized);
    }
}
