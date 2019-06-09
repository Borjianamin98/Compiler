package semantic.symbolTable.descriptor.hastype;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

public class HasTypeDSCP extends DSCP {
    private TypeDSCP type;
    private boolean constant;
    private boolean initialized;

    public HasTypeDSCP(String name, TypeDSCP type, boolean constant, boolean initialized) {
        super(name);
        this.type = type;
        this.constant = constant;
        this.initialized = initialized;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(getType(), 0);
    }

    public boolean isConstant() {
        return constant;
    }

    public TypeDSCP getType() {
        return type;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
