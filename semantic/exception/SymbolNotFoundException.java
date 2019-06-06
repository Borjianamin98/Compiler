package semantic.exception;

public class SymbolNotFoundException extends RuntimeException {
    public SymbolNotFoundException() {
        super("Symbol not found in symbol table");
    }

    public SymbolNotFoundException(String message) {
        super(message);
    }
}
