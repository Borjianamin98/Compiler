package semantic.symbolTable.descriptor;

public class VariableDSCP extends SizedDSCP {
    private int address;
    private boolean isConstant;

    public VariableDSCP(int type, int size, int address, boolean isConstant) {
        super(type, size);
        this.address = address;
        this.isConstant = isConstant;
    }

    public int getAddress() {
        return address;
    }

    public boolean isConstant() {
        return isConstant;
    }
}
