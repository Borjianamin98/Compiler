package semantic.syntaxTree.expression.operation.bitwise;

import semantic.syntaxTree.expression.Expression;

public class BitwiseAnd extends Bitwise {
    public BitwiseAnd(Expression firstOperand, Expression secondOperand) {
        super("&", "AND", firstOperand, secondOperand);
    }
}
