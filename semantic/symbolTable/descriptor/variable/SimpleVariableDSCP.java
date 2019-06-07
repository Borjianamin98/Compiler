package semantic.symbolTable.descriptor.variable;

import semantic.symbolTable.descriptor.type.TypeDSCP;

public class SimpleVariableDSCP extends VariableDSCP {
    private boolean initialized;

    public SimpleVariableDSCP(String name, TypeDSCP type, int size, int address, boolean constant, boolean initialized) {
        super(name, type, size, address, constant);
        this.initialized = initialized;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
