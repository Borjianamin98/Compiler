package semantic.symbolTable;

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
//    private int tempNumber;

    public SymbolTable() {
        symbols = new HashMap<>();
        freeAddress = 0;
//        tempNumber = 0;
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

    public void addType(String name, TypeDSCP descriptor) {
        descriptor.setTypeCode(freeType);
        freeType++;
        symbols.put(name, descriptor);
    }

    public boolean contain(String name) {
        return symbols.containsKey(name);
    }

    public DSCP getDSCP(String name) {
        if (!symbols.containsKey(name))
            throw new SymbolNotFoundException();
        return symbols.get(name);
    }

//    private DSCP addSymbolDSCP(String name, DSCP descriptor) {
//        if (!symbols.containsKey(name))
//            throw new SymbolNotFoundException();
//        symbols.put(name, descriptor);
//        return descriptor;
//    }

//    public String getTemp(int type) {
////        String tempName = "$temp_" + tempNumber;
////        switch (type) {
////            case Constants.INTEGER_CODE:
////                addSymbol(tempName, new DSCP(Constants.INTEGER_CODE, Constants.INTEGER_SIZE, freeAddress));
////                freeAddress += Constants.INTEGER_SIZE;
////                break;
////            case Constants.LONG_CODE:
////                addSymbol(tempName, new DSCP(Constants.LONG_CODE, Constants.LONG_SIZE, freeAddress));
////                freeAddress += Constants.LONG_SIZE;
////                break;
////            case Constants.REAL_CODE:
////                addSymbol(tempName, new DSCP(Constants.REAL_CODE, Constants.REAL_SIZE, freeAddress));
////                freeAddress += Constants.REAL_SIZE;
////                break;
////            case Constants.STRING_CODE:
////                // TODO
////                break;
////            case Constants.VOID_CODE:
////                // TODO
////                // do nothing
////                break;
////            default:
////                addSymbol(tempName, new DSCP(type, Constants.REFERENCE_SIZE, freeAddress));
////                freeAddress += Constants.REFERENCE_SIZE;
////        }
////        return tempName;
//    }

    public int getFreeAddress() {
        return freeAddress;
    }

}
