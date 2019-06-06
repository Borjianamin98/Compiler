package semantic.syntaxTree.declaration;

import semantic.syntaxTree.Node;
import semantic.syntaxTree.expression.Expression;

public abstract class Declaration extends Node {
    private String name;
    private String type;
    private boolean isConstant;
    private Expression defaultValue;

    public Declaration(String name, String type, boolean isConstant, Expression defaultValue) {
        this.name = name;
        this.type = type;
        this.isConstant = isConstant;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }
}
