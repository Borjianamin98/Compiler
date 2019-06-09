package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;

public abstract class TypeDSCP extends DSCP {
    private int typeCode;
    private int size;
    private int dimensions;
    private boolean isPrimitive;

    public TypeDSCP(String name, int size, int dimensions, boolean isPrimitive) {
        super(name);
        this.size = size;
        this.dimensions = dimensions;
        this.isPrimitive = isPrimitive;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(this, dimensions);
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

    public int getDimensions() {
        return dimensions;
    }
}
