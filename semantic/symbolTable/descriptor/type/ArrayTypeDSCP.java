package semantic.symbolTable.descriptor.type;

public class ArrayTypeDSCP extends TypeDSCP {
    private int arrayLevel;
    private TypeDSCP internalType;
    private TypeDSCP originType;

    public ArrayTypeDSCP(int arrayLevel, TypeDSCP internalType, TypeDSCP originType) {
        super("[" + internalType.getDescriptor(), 1, false);
        this.arrayLevel = arrayLevel;
        this.internalType = internalType;
        this.originType = originType;
    }

    public int getArrayLevel() {
        return arrayLevel;
    }

    public TypeDSCP getOriginType() {
        return originType;
    }

    @Override
    public String getDescriptor() {
        return getName();
    }

    public TypeDSCP getInternalType() {
        return internalType;
    }
}
