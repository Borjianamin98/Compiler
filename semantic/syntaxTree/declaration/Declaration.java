package semantic.syntaxTree.declaration;

import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.Node;

public abstract class Declaration extends Node implements BlockCode {
    private String name;
    private boolean isConstant;

    public Declaration(String name, boolean isConstant) {
        this.name = name;
        this.isConstant = isConstant;
    }

    public String getName() {
        return name;
    }

    public boolean isConstant() {
        return isConstant;
    }
}
