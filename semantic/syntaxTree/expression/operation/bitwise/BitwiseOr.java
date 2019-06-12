package semantic.syntaxTree.expression.operation.bitwise;

import semantic.syntaxTree.expression.Expression;

public class BitwiseOr extends Bitwise {
    public BitwiseOr(Expression firstOperand, Expression secondOperand) {
        super("|", "OR", firstOperand, secondOperand);
    }
}
