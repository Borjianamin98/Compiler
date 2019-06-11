package semantic.syntaxTree.expression.operation.arithmetic;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Arithmetic extends BinaryOperation {
    private String arithmeticSign;
    private TypeDSCP resultType;

    public Arithmetic(String arithmeticSign, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.arithmeticSign = arithmeticSign;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive() ||
                    TypeTree.isString(getFirstOperand().getResultType()) || TypeTree.isString(getSecondOperand().getResultType()) ||
                    getFirstOperand().getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode() ||
                    getSecondOperand().getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode())
                throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                        getArithmeticSign(), getFirstOperand().getResultType().getConventionalName(), getSecondOperand().getResultType().getConventionalName()));
            resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
        }
        return resultType;
    }

    public String getArithmeticSign() {
        return arithmeticSign;
    }
}
