package semantic.syntaxTree.declaration.method;

import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Optional;

public class Argument {
    private String name;
    private String baseType;
    private TypeDSCP baseTypeDSCP;
    private int dimensions;

    public Argument(String name, String baseType, int dimensions) {
        this.name = name;
        this.dimensions = dimensions;
        this.baseType = baseType;
    }

    public boolean isArray() {
        return dimensions > 0;
    }

    public String getName() {
        return name;
    }

    public String getBaseType() {
        return baseType;
    }

    public int getDimensions() {
        return dimensions;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(getBaseTypeDSCP(), dimensions);
    }

    public TypeDSCP getBaseTypeDSCP() {
        if (baseTypeDSCP == null) {
            Optional<DSCP> typeDSCP = Display.find(baseType);
            if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException(baseType + " is not declared");
            baseTypeDSCP =((TypeDSCP) typeDSCP.get());
        }
        return baseTypeDSCP;
    }

    public TypeDSCP getType() {
        Optional<DSCP> typeDSCP = Display.find(getDescriptor());
        if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
            throw new SymbolNotFoundException(baseType + " is not declared");
        return ((TypeDSCP) typeDSCP.get());
    }
}
