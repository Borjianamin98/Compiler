package semantic.symbolTable.descriptor;

public class FieldDSCP extends SizedDSCP {
    private String name;
    private boolean isConstant;

    public FieldDSCP(int type, int size, String name, boolean isConstant) {
        super(type, size);
        this.name = name;
        this.isConstant = isConstant;
    }

    public String getName() {
        return name;
    }

    public boolean isConstant() {
        return isConstant;
    }
}
