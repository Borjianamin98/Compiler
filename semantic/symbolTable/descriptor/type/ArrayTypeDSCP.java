package semantic.symbolTable.descriptor.type;

public class ArrayTypeDSCP extends TypeDSCP {
    private TypeDSCP internalType;

    public ArrayTypeDSCP(TypeDSCP internalType) {
        super("[" + internalType.getDescriptor(), 1, false);
        this.internalType = internalType;
    }

    @Override
    public String getDescriptor() {
        return getName();
    }

    public TypeDSCP getInternalType() {
        return internalType;
    }
}
