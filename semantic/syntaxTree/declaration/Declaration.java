package semantic.syntaxTree.declaration;

import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public abstract class Declaration extends Node {
    private String name;
    private String type;
    private TypeDSCP typeDSCP;
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

    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            // Only check current block table
            // otherwise this declaration shadows other declarations
            SymbolTable top = Display.top();
            if (top.contain(getName())) {
                throw new DuplicateDeclarationException(getName() + " declared more than one time");
            }
            Optional<DSCP> fetchedDSCP = Display.find(getType());
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException();
            typeDSCP = (TypeDSCP) fetchedDSCP.get();
        }
        return typeDSCP;
    }
}
