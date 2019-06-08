package semantic.symbolTable;

import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;

import java.util.HashMap;

public class SymbolTable {
    public static SymbolTable symbolTable = new SymbolTable();
    private static int freeType;

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

    public void addSymbol(String name, DSCP descriptor) {
        if (descriptor instanceof TypeDSCP)
            throw new RuntimeException("TypeDSCP must add through addType to SymbolTable");
        if (descriptor instanceof VariableDSCP)
            freeAddress += ((VariableDSCP) descriptor).getSize();
        symbols.put(name, descriptor);
    }

    public static void addType(String name, TypeDSCP descriptor) {
        if (Display.mainSymbolTable.symbols.containsKey(name)) {
            throw new DuplicateDeclarationException("Type " + name + " declared more than once");
        }
        // TODO add all types to main symbol table
        descriptor.setTypeCode(freeType);
        freeType++;
        Display.mainSymbolTable.symbols.put(name, descriptor);
    }

    public static TypeDSCP getType(String name) {
        return (TypeDSCP) Display.mainSymbolTable.symbols.get(name);
    }

    public boolean contain(String name) {
        return symbols.containsKey(name);
    }

    public DSCP getDSCP(String name) {
        if (!symbols.containsKey(name))
            throw new SymbolNotFoundException();
        return symbols.get(name);
    }

    public String getTemp(TypeDSCP type) {
        String tempName = "$" + tempNumber;
        addSymbol(tempName, new VariableDSCP(tempName, type, type.getSize(), freeAddress, false, false));
        return tempName;
    }

    public int getFreeAddress() {
        return freeAddress;
    }

}
