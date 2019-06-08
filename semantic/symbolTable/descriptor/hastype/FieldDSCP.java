package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class FieldDSCP extends HasTypeDSCP {
    public FieldDSCP(String name, TypeDSCP type, int arrayLevel, boolean constant, boolean initialized) {
        super(name, type, arrayLevel, constant, initialized);
    }
}
