package semantic.syntaxTree.declaration.record;

import semantic.syntaxTree.declaration.Parameter;
import semantic.syntaxTree.expression.Expression;

public class Field extends Parameter {
    private Expression defaultValue;
    private boolean constant;
    private boolean isStatic;

    public Field(String name, String baseType, int dimensions, Expression defaultValue, boolean constant, boolean isStatic) {
        super(name, baseType, dimensions);
        this.constant = constant;
        this.defaultValue = defaultValue;
        this.isStatic = isStatic;
    }

    public Field(String name, String baseType, int dimensions, boolean constant, boolean isStatic) {
        this(name, baseType, dimensions, null, constant, isStatic);
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public boolean isConstant() {
        return constant;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
