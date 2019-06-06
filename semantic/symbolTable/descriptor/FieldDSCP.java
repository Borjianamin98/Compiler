package semantic.symbolTable.descriptor;

public class FieldDSCP extends DSCP {
    private TypeDSCP type;
    private int size;
    private boolean isConstant;

    public FieldDSCP(String name, TypeDSCP type, int size, boolean isConstant) {
        super(name);
        this.type = type;
        this.size = size;
        this.isConstant = isConstant;
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
