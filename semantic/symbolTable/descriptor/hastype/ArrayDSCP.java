package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;

public class ArrayDSCP extends VariableDSCP {
    private int dimensions;
    private TypeDSCP content;
    private TypeDSCP baseType;

    public ArrayDSCP(String name, TypeDSCP type, TypeDSCP content, TypeDSCP baseType, boolean constant) {
        super(name, type, 1, -1, constant, true);
        this.content = content;
        this.baseType = baseType;
        this.dimensions = content.getDimensions() + 1;
    }

    public ArrayDSCP(String name, TypeDSCP type, TypeDSCP content, TypeDSCP baseType, int address, boolean constant) {
        super(name, type, 1, address, constant, true);
        this.content = content;
        this.baseType = baseType;
        this.dimensions = content.getDimensions() + 1;
    }

    @Override
    public int getAddress() {
        if (super.getAddress() == -1)
            throw new RuntimeException("ArrayDSCP doesn't contain address");
        return super.getAddress();
    }

    public TypeDSCP getBaseType() {
        return baseType;
    }

    public int getDimensions() {
        return dimensions;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(baseType, dimensions);
    }
}
