package semantic.exception;

public class ConstantModificationException extends RuntimeException {
    public ConstantModificationException() {
        super("Can't modify constant variable");
    }

    public ConstantModificationException(String message) {
        super(message);
    }
}
