package semantic.symbolTable.descriptor.type;

import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;

import java.util.Optional;

public class RecordTypeDSCP extends TypeDSCP {
    private SymbolTable symbolTable;

    public RecordTypeDSCP(String name, int size, SymbolTable symbolTable) {
        super(name, size, 0, false);
        this.symbolTable = symbolTable;
    }

    public Optional<DSCP> find(String name) {
        return symbolTable.getDSCP(name);
    }

    public Optional<HasTypeDSCP> getField(String fieldName) {
        return symbolTable.getDSCP(fieldName).map(dscp -> (HasTypeDSCP) dscp);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
