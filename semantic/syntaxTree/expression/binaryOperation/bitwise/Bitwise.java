package semantic.syntaxTree.expression.binaryoperation.bitwise;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryoperation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Bitwise extends BinaryOperation {
    private String bitwiseSign;
    private TypeDSCP resultType;

    public Bitwise(String bitwiseSign, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.bitwiseSign = bitwiseSign;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive() ||
                    !TypeTree.isInteger(getFirstOperand().getResultType()) || !TypeTree.isInteger(getSecondOperand().getResultType()))
                throw new RuntimeException(String.format("Bad operand types for bitwise operator '%s'\n  first type: %s\n  second type: %s",
                        getBitwiseSign(), getFirstOperand().getResultType().getName(), getSecondOperand().getResultType().getName()));
            resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
        }
        return resultType;
    }

    public String getBitwiseSign() {
        return bitwiseSign;
    }
}
