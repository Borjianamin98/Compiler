package semantic.syntaxTree.expression.operation.arithmetic;

import semantic.syntaxTree.expression.Expression;

public class Reminder extends Arithmetic {
    public Reminder(Expression firstOperand, Expression secondOperand) {
        super("%", "REM", firstOperand, secondOperand);
    }
}
