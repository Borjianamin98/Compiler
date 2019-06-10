package semantic.syntaxTree.expression.binaryOperation.bitwise;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Bitwise extends BinaryOperation {
    private String bitwiseSign;

    public Bitwise(String bitwiseSign, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.bitwiseSign = bitwiseSign;
    }

    @Override
    public TypeDSCP getResultType() {
        if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive() ||
                !TypeTree.isInteger(getFirstOperand().getResultType()) || !TypeTree.isInteger(getSecondOperand().getResultType()))
            throw new RuntimeException(String.format("Bad operand types for bitwise operator '%s'\n  first type: %s\n  second type: %s",
                    getBitwiseSign(), getFirstOperand().getResultType().getName(), getSecondOperand().getResultType().getName()));
        return TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
    }

    public String getBitwiseSign() {
        return bitwiseSign;
    }
}
