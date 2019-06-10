package semantic.syntaxTree.declaration;

import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Optional;

public class Parameter {
    protected String name;
    private String baseType;
    private int dimensions;
    private TypeDSCP baseTypeDSCP;

    public Parameter(String name, String baseType, int dimensions) {
        this.name = name;
        this.baseType = baseType;
        this.dimensions = dimensions;
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
        Optional<DSCP> typeDSCP;
        if (getBaseTypeDSCP().isPrimitive() && !isArray())
            typeDSCP = Display.find(getBaseTypeDSCP().getName());
        else
            typeDSCP = Display.find(getDescriptor());
        if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
            throw new SymbolNotFoundException(getDescriptor() + " is not declared");
        return ((TypeDSCP) typeDSCP.get());
    }
}
