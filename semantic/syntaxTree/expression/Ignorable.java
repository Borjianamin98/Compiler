package semantic.syntaxTree.expression;

/**
 * if an expression can be used but result of that can be ignored,
 * it must implement this interface and handle it correctly by pop
 * it's result from operand stack
 */
public interface Ignorable {
    void setIgnoreResult(boolean ignoreResult);
}
