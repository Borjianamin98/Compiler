package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class VariableDSCP extends HasTypeDSCP {
    private int size;
    private int address;
    private boolean addressable;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, boolean initialized) {
        super(name, type, constant, initialized);
        this.size = size;
        this.address = address;
        addressable = address >= 0;
    }

    public int getAddress() {
        return address;
    }

    public boolean isAddressable() {
        return addressable;
    }

    public int getSize() {
        return size;
    }

}
