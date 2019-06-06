package semantic.exception;

public class TypeMismatchException extends RuntimeException {
    public TypeMismatchException() {
        super();
    }

    public TypeMismatchException(String message) {
        super(message);
    }
}
