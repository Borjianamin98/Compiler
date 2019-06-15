package exception;

public class DuplicateDeclarationException extends RuntimeException {

    public DuplicateDeclarationException(String duplicateSymbol) {
        super("'" + duplicateSymbol + "' declared more than once");
    }

}
