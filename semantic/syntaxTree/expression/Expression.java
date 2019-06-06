package semantic.syntaxTree.expression;

import semantic.symbolTable.descriptor.TypeDSCP;
import semantic.syntaxTree.Node;

public abstract class Expression extends Node {
    private TypeDSCP resultType;

    public Expression() {

    }

    public Expression(TypeDSCP resultType) {
        this.resultType = resultType;
    }

    public TypeDSCP getResultType() {
        return resultType;
    }

    public void setResultType(TypeDSCP resultType) {
        this.resultType = resultType;
    }
}
