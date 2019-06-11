package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.typeTree.TypeTree;

import java.util.Objects;

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
        switch ((TypeTree.typePrefix + nameString)) {
            case TypeTree.INTEGER_NAME:
                convName.insert(0, "int");
                break;
            case TypeTree.BOOLEAN_NAME:
                convName.insert(0, "bool");
                break;
            case TypeTree.LONG_NAME:
                convName.insert(0, "long");
                break;
            case TypeTree.FLOAT_NAME:
                convName.insert(0, "float");
                break;
            case TypeTree.DOUBLE_NAME:
                convName.insert(0, "double");
                break;
            case TypeTree.CHAR_NAME:
                convName.insert(0, "char");
                break;
            case TypeTree.STRING_NAME:
                convName.insert(0, "string");
                break;
            case TypeTree.VOID_NAME:
                convName.insert(0, "void");
                break;
            default:
                convName.insert(0, name.substring(1, name.length() - 1)); // name is like: L(type);
                break;
        }
        return convName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeDSCP typeDSCP = (TypeDSCP) o;
        return typeCode == typeDSCP.typeCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCode);
    }
}
