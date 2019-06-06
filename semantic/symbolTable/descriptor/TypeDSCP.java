package semantic.symbolTable.descriptor;

public class TypeDSCP extends SizedDSCP {
    private boolean isPrimitive;

    public TypeDSCP(int type, int size, boolean isPrimitive) {
        super(type, size);
        this.isPrimitive = isPrimitive;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }
}
