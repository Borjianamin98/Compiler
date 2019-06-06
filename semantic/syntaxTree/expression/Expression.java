package semantic.syntaxTree.expression;

import semantic.syntaxTree.Node;

public abstract class Expression extends Node {
    private int resultType;

    public Expression() {

    }

    public Expression(int resultType) {
        this.resultType = resultType;
    }

    public int getResultType() {
        return resultType;
    }

    public int setResultType(int resultType) {
        return this.resultType = resultType;
    }
}
