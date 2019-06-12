package semantic.syntaxTree.expression.operation.bitwise;

import semantic.syntaxTree.expression.Expression;

public class BitwiseXOR extends Bitwise {
    public BitwiseXOR(Expression firstOperand, Expression secondOperand) {
        super("^", "XOR", firstOperand, secondOperand);
    }
}
