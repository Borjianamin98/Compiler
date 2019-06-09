package semantic.syntaxTree.declaration;

import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public abstract class Declaration extends Node implements BlockCode {
    private String name;
    private String type;
    protected TypeDSCP typeDSCP;
    private boolean isConstant;

    public Declaration(String name, String type, boolean isConstant) {
        this.name = name;
        this.type = type;
        this.isConstant = isConstant;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public abstract TypeDSCP getTypeDSCP();
}
