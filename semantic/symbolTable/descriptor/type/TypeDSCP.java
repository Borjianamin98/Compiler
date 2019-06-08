package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.descriptor.DSCP;

public class TypeDSCP extends DSCP {
    private int typeCode;
    private int size;
    private boolean isPrimitive;

    public TypeDSCP(String name, int size, boolean isPrimitive) {
        super(name);
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

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }
}
