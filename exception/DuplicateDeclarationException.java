package exception;

public class DuplicateDeclarationException extends RuntimeException {
    private String duplicateSymbol;

    public DuplicateDeclarationException(String duplicateSymbol) {
        super("'" + duplicateSymbol + "' declared more than once");
        this.duplicateSymbol = duplicateSymbol;
    }

    public String getDuplicateSymbol() {
        return duplicateSymbol;
    }
}
