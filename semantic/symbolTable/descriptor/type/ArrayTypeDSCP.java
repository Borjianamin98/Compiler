package semantic.symbolTable.descriptor.type;

public class ArrayTypeDSCP extends TypeDSCP {
    private TypeDSCP internalType;
    private TypeDSCP baseType;

    public ArrayTypeDSCP(TypeDSCP internalType, TypeDSCP baseType) {
        super("[" + internalType.getDescriptor(), 1, internalType.getDimensions() + 1, false);
        this.internalType = internalType;
        this.baseType = baseType;
    }

    public TypeDSCP getBaseType() {
        return baseType;
    }

    @Override
    public String getDescriptor() {
        return getName();
    }

    public TypeDSCP getInternalType() {
        return internalType;
    }
}
