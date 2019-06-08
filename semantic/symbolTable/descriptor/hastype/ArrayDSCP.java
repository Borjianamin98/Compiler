package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class ArrayDSCP extends HasTypeDSCP {
    private TypeDSCP content;

    public ArrayDSCP(String name, TypeDSCP type, TypeDSCP content, boolean constant) {
        super(name, type, constant, true);
        this.content = content;
    }

    public String getDescriptor() {
        return "[" + content.getDescriptor();
    }
}
