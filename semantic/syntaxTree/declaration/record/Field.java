package semantic.syntaxTree.declaration.record;

import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.Parameter;
import semantic.syntaxTree.expression.Expression;

public class Field extends Parameter implements ClassCode {
    private Expression defaultValue;
    private boolean constant;
    private boolean isStatic;

    /**
     * create a field for class/record based on available information
     * @param name name of field
     * @param baseType base type of field
     * @param dimensions dimensions of field
     * @param defaultValue default value of field (if null, meaning no initialization)
     * @param constant true if field is constant (final)
     * @param isStatic true if field is static
     */
    public Field(String name, String baseType, int dimensions, Expression defaultValue, boolean constant, boolean isStatic) {
        super(name, baseType, dimensions);
        this.constant = constant;
        this.defaultValue = defaultValue;
        this.isStatic = isStatic;
    }

    /**
     * create a field for class/record based on available information
     * type of field detected automatically from initialization (Auto field declaration)
     * @param name name of field
     * @param defaultValue default value of field
     * @param constant true if field is constant (final)
     * @param isStatic true if field is static
     */
    public Field(String name, Expression defaultValue, boolean constant, boolean isStatic) {
        super(name, defaultValue.getResultType().getName(), 0);
        this.constant = constant;
        this.defaultValue = defaultValue;
        this.isStatic = isStatic;
        if (defaultValue.getResultType() instanceof SimpleTypeDSCP ||
                defaultValue.getResultType() instanceof RecordTypeDSCP) {
            // nothing to do
        } else if (defaultValue.getResultType() instanceof ArrayTypeDSCP) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) defaultValue.getResultType();
            baseType = arrayTypeDSCP.getBaseType().getName();
            dimensions = arrayTypeDSCP.getDimensions();
        } else {
            throw new RuntimeException("Can not detect type for auto variable");
        }
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
