package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class ArrayDSCP extends HasTypeDSCP {
    private int arrayLevel;
    private TypeDSCP content;
    private TypeDSCP originType;

    public ArrayDSCP(String name, TypeDSCP type, TypeDSCP content, TypeDSCP originType, int arrayLevel, boolean constant) {
        super(name, type, constant, true);
        this.content = content;
        this.originType = originType;
        this.arrayLevel = arrayLevel;
    }

    /**
     * @return original array type
     */
    public TypeDSCP getOriginType() {
        return originType;
    }

    public int getArrayLevel() {
        return arrayLevel;
    }

    public String getDescriptor() {
        return "[" + content.getDescriptor();
    }
}
