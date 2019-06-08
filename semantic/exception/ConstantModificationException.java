package semantic.exception;

public class ConstantModificationException extends RuntimeException {
    public ConstantModificationException() {
        super("Can't modify constant hastype");
    }

    public ConstantModificationException(String message) {
        super(message);
    }
}
