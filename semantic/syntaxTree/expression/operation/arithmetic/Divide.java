package semantic.syntaxTree.expression.operation.arithmetic;

import semantic.syntaxTree.expression.Expression;

public class Divide extends Arithmetic {
    public Divide(Expression firstOperand, Expression secondOperand) {
        super("/", "DIV", firstOperand, secondOperand);
    }
}
