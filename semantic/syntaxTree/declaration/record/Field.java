package semantic.syntaxTree.declaration.record;

import semantic.syntaxTree.declaration.Parameter;
import semantic.syntaxTree.expression.Expression;

public class Field extends Parameter {
    private Expression defaultValue;
    private boolean constant;

    public Field(String name, String baseType, int dimensions, Expression defaultValue, boolean constant) {
        super(name, baseType, dimensions);
        this.constant = constant;
        this.defaultValue = defaultValue;
    }

    public Field(String name, String baseType, int dimensions, boolean constant) {
        this(name, baseType, dimensions, null, constant);
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public boolean isConstant() {
        return constant;
    }
}
