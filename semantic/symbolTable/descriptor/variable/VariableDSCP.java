package semantic.symbolTable.descriptor.variable;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class VariableDSCP extends HasTypeDSCP {
    private int size;
    private int address;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, boolean initialized) {
        super(name, type, 0, constant, initialized);
        this.size = size;
        this.address = address;
    }

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, int arrayLevel) {
        super(name, type, arrayLevel, constant, true);
        this.size = size;
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public int getSize() {
        return size;
    }

}
