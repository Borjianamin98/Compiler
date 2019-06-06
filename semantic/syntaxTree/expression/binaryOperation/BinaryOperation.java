package semantic.syntaxTree.expression.binaryOperation;

import semantic.syntaxTree.expression.Expression;

public abstract class BinaryOperation extends Expression {
    private Expression firstOperand;
    private Expression secondOperand;
    private int resultType;

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

    public int getResultType() {
        return resultType;
    }

    public int setResultType(int resultType) {
        return this.resultType = resultType;
    }
}
