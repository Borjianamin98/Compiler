package semantic.syntaxTree.expression.operation;

import semantic.syntaxTree.expression.Expression;

public abstract class BinaryOperation extends Expression {
    private Expression firstOperand;
    private Expression secondOperand;

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

}
