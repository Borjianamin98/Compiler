package semantic.symbolTable;

import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;

import java.util.HashMap;
import java.util.Optional;

public class SymbolTable {
    public static SymbolTable symbolTable = new SymbolTable();

    private HashMap<String, DSCP> symbols;
    private int freeAddress;
    private int tempNumber;

    public SymbolTable() {
        symbols = new HashMap<>();
        freeAddress = 0;
        tempNumber = 0;
    }

    public SymbolTable(int startAddress) {
        this();
        freeAddress = startAddress;
    }

    public HashMap<String, DSCP> getSymbols() {
        return symbols;
    }

    public void addSymbol(String name, DSCP descriptor) {
        if (descriptor instanceof TypeDSCP)
            throw new RuntimeException("TypeDSCP must add through addType to SymbolTable");
        if (descriptor instanceof VariableDSCP && ((VariableDSCP) descriptor).isAddressable())
            freeAddress += ((VariableDSCP) descriptor).getSize();
        symbols.put(name, descriptor);
    }

    public static TypeDSCP getType(String name) {
        return (TypeDSCP) Display.mainSymbolTable.symbols.get(name);
    }

    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    /**
     * get a symbol form current symbol table
     * @param name name of symbol
     * @return symbol DSCP if found, otherwise Optional.empty
     */
    public Optional<DSCP> getDSCP(String name) {
        return Optional.ofNullable(symbols.get(name));
    }

    public String getTempName() {
        String tempName = "temp$" + tempNumber;
        tempNumber++;
        return tempName;
    }

    public int getFreeAddress() {
        return freeAddress;
    }

}
