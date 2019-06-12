package semantic.syntaxTree.expression.operation.arithmetic;

import semantic.syntaxTree.expression.Expression;

public class Multiply extends Arithmetic {
    public Multiply(Expression firstOperand, Expression secondOperand) {
        super("*", "MUL", firstOperand, secondOperand);
    }
}
