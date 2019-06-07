package semantic.symbolTable.descriptor;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.symbolTable.descriptor.variable.HasTypeDSCP;

public class FieldDSCP extends HasTypeDSCP {
    public FieldDSCP(String name, TypeDSCP type, int arrayLevel, boolean constant, boolean initialized) {
        super(name, type, arrayLevel, constant, initialized);
    }
}
