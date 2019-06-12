package semantic.syntaxTree.declaration;

import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Collections;
import java.util.Optional;

public class Parameter {
    protected String name;
    protected String baseType;
    protected int dimensions;
    protected TypeDSCP baseTypeDSCP;

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
        if (baseTypeDSCP == null)
            baseTypeDSCP = Display.getType(baseType);
        return baseTypeDSCP;
    }

    public TypeDSCP getType() {
        return Display.getType(String.join("", Collections.nCopies(dimensions, "[")) + getBaseTypeDSCP().getName());
    }
}
