package semantic.exception;

public class IllegalTypeException extends RuntimeException {
    public IllegalTypeException() {
        super("The type is used in the wrong place");
    }

    public IllegalTypeException(String message) {
        super(message);
    }
}
