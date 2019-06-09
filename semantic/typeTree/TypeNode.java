package semantic.typeTree;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class TypeNode {
    private TypeNode parent;
    private TypeDSCP type;
    private int level;

    public TypeNode(TypeNode parent, TypeDSCP type) {
        this.parent = parent;
        this.type = type;
        this.level = parent.level + 1;
    }

    public TypeNode(TypeNode parent, TypeDSCP type, int level) {
        this.parent = parent;
        this.type = type;
        this.level = level;
    }

    public TypeNode getParent() {
        return parent;
    }

    public TypeDSCP getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
}
