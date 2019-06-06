package semantic.symbolTable.descriptor;

public class VariableDSCP extends DSCP {
    private TypeDSCP type;
    private int size;
    private int address;
    private boolean isConstant;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean isConstant) {
        super(name);
        this.type = type;
        this.size = size;
        this.address = address;
        this.isConstant = isConstant;
    }

    public int getAddress() {
        return address;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public TypeDSCP getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
