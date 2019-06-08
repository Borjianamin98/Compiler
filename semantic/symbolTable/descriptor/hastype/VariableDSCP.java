package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class VariableDSCP extends HasTypeDSCP {
    private int size;
    private int address;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, boolean initialized) {
        super(name, type, constant, initialized);
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
