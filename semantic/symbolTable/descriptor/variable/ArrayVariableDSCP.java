package semantic.symbolTable.descriptor.variable;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class ArrayVariableDSCP extends VariableDSCP {
    private int arrayLevels;

    public ArrayVariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, int arrayLevels) {
        super(name, type, size, address, constant);
        this.arrayLevels = arrayLevels;
    }

    public int getArrayLevels() {
        return arrayLevels;
    }
}
