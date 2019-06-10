package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.typeTree.TypeTree;

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

    public String getConventionalName() {
        StringBuilder name = new StringBuilder(getDescriptor());
        StringBuilder convName = new StringBuilder();
        while (name.charAt(0) == '[') {
            convName.append("[]");
            name.deleteCharAt(0);
        }
        String nameString = name.toString();
        if (nameString.equals(TypeTree.INTEGER_NAME))
            convName.insert(0, "int");
        else if (nameString.equals(TypeTree.BOOLEAN_NAME))
            convName.insert(0, "bool");
        else if (nameString.equals(TypeTree.LONG_NAME))
            convName.insert(0, "long");
        else if (nameString.equals(TypeTree.FLOAT_NAME))
            convName.insert(0, "float");
        else if (nameString.equals(TypeTree.DOUBLE_NAME))
            convName.insert(0, "double");
        else if (nameString.equals(TypeTree.CHAR_NAME))
            convName.insert(0, "char");
        else if (nameString.equals(TypeTree.STRING_NAME))
            convName.insert(0, "string");
        else if (nameString.equals(TypeTree.VOID_NAME))
            convName.insert(0, "void");
        else
            convName.insert(0, name.substring(1, name.length() - 1)); // name is like: L(type);
        return convName.toString();
    }
}
