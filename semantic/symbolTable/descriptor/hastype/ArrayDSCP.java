package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class ArrayDSCP extends HasTypeDSCP {
    private int length;
    private TypeDSCP content;

    public ArrayDSCP(String name, TypeDSCP type, TypeDSCP content, int length, boolean constant) {
        super(name, type, constant, true);
        this.content = content;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public String getDescriptor() {
        return "[" + content.getDescriptor();
    }
}
