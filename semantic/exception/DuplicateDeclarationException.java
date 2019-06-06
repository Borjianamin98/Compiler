package semantic.exception;

public class DuplicateDeclarationException extends RuntimeException {
    public DuplicateDeclarationException() {
        super("Symbol declared more than one time");
    }

    public DuplicateDeclarationException(String message) {
        super(message);
    }
}
