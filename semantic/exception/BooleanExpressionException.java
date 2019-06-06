package semantic.exception;

public class BooleanExpressionException extends RuntimeException {
    public BooleanExpressionException() {
        super("You must provide boolean expression for conditions");
    }
}
