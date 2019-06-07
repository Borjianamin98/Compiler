package semantic.symbolTable.descriptor.variable;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

public class HasTypeDSCP extends DSCP {
    private TypeDSCP type;
    private int arrayLevel;
    private boolean constant;
    private boolean initialized;
    private boolean isArray;

    public HasTypeDSCP(String name, TypeDSCP type, int arrayLevel, boolean constant, boolean initialized) {
        super(name);
        this.type = type;
        this.constant = constant;
        this.initialized = initialized;
        this.arrayLevel = arrayLevel;
        if (arrayLevel > 0)
            this.isArray = true;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(getType(), arrayLevel);
    }

    public boolean isConstant() {
        return constant;
    }

    public TypeDSCP getType() {
        return type;
    }

    public int getArrayLevel() {
        return arrayLevel;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
