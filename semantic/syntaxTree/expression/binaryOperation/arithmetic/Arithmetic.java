package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
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
                    TypeTree.isString(getFirstOperand().getResultType()) || TypeTree.isString(getSecondOperand().getResultType()))
                throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                        getArithmeticSign(), getFirstOperand().getResultType().getName(), getSecondOperand().getResultType().getName()));
            resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());;
        }
        return resultType;
    }

    public String getArithmeticSign() {
        return arithmeticSign;
    }
}
