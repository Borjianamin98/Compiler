package semantic.syntaxTree.expression.binaryOperation;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;

public abstract class BinaryOperation extends Expression {
    private Expression firstOperand;
    private Expression secondOperand;
    private TypeDSCP resultType;

    public BinaryOperation(Expression firstOperand, Expression secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public Expression getFirstOperand() {
        return firstOperand;
    }

    public Expression getSecondOperand() {
        return secondOperand;
    }

    public TypeDSCP getResultType() {
        return resultType;
    }

    public void setResultType(TypeDSCP resultType) {
        this.resultType = resultType;
    }
}
