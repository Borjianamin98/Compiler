package semantic.syntaxTree.expression;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.Node;

public abstract class Expression extends Node {
    public abstract TypeDSCP getResultType();
}
