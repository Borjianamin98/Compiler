package semantic.symbolTable.descriptor;

public class TypeDSCP extends DSCP {
    private int typeCode;
    private int size;
    private boolean isPrimitive;

    public TypeDSCP(String name, int typeCode, int size, boolean isPrimitive) {
        super(name);
        this.typeCode = typeCode;
        this.size = size;
        this.isPrimitive = isPrimitive;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public int getSize() {
        return size;
    }
}
