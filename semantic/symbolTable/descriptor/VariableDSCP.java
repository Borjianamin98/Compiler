package semantic.symbolTable.descriptor;

public class VariableDSCP extends DSCP {
    private TypeDSCP type;
    private int size;
    private int address;
    private boolean constant;
    private boolean initialized;

    public VariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, boolean initialized) {
        super(name);
        this.type = type;
        this.size = size;
        this.address = address;
        this.constant = constant;
        this.initialized = initialized;
    }

    public int getAddress() {
        return address;
    }

    public boolean isConstant() {
        return constant;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public TypeDSCP getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
