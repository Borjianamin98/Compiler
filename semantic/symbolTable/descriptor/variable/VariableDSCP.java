package semantic.symbolTable.descriptor.variable;

import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

public class VariableDSCP extends DSCP {
    private TypeDSCP type;
    private int size;
    private int address;
    private boolean constant;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant) {
        super(name);
        this.type = type;
        this.size = size;
        this.address = address;
        this.constant = constant;
    }

    public int getAddress() {
        return address;
    }

    public boolean isConstant() {
        return constant;
    }

    public TypeDSCP getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
