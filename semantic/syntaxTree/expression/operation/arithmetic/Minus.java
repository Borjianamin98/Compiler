package semantic.syntaxTree.expression.operation.arithmetic;

import semantic.syntaxTree.expression.Expression;

public class Minus extends Arithmetic {
    public Minus(Expression firstOperand, Expression secondOperand) {
        super("-", "SUB", firstOperand, secondOperand);
    }
}
