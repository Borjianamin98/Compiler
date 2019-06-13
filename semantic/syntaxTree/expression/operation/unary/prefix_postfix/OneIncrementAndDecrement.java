package semantic.syntaxTree.expression.operation.unary.prefix_postfix;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.symbolTable.typeTree.TypeTree;

public abstract class OneIncrementAndDecrement extends Expression {
    private String operatorName;
    private Variable variable;

    public OneIncrementAndDecrement(String operatorName, Variable variable) {
        this.operatorName = operatorName;
        this.variable = variable;
    }

    @Override
    public TypeDSCP getResultType() {
        if (!variable.getResultType().isPrimitive() || TypeTree.isString(variable.getResultType())
                || variable.getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode())
            throw new RuntimeException(String.format("Bad operand types for %s operator: %s",
                    operatorName, variable.getResultType().getConventionalName()));
        return variable.getResultType();
    }

    public Variable getVariable() {
        return variable;
    }

}
