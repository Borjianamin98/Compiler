package semantic.symbolTable;

import semantic.symbolTable.descriptor.DSCP;
import semantic.typeTree.TypeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Display {
    private static List<SymbolTable> displayList = new ArrayList<>();
    static SymbolTable mainSymbolTable = new SymbolTable();

    static {
        SymbolTable.addType(TypeTree.INTEGER_NAME, TypeTree.INTEGER_DSCP);
        SymbolTable.addType(TypeTree.BOOLEAN_NAME, TypeTree.BOOLEAN_DSCP);
        SymbolTable.addType(TypeTree.LONG_NAME, TypeTree.LONG_DSCP);
        SymbolTable.addType(TypeTree.FLOAT_NAME, TypeTree.FLOAT_DSCP);
        SymbolTable.addType(TypeTree.DOUBLE_NAME, TypeTree.DOUBLE_DSCP);
        SymbolTable.addType(TypeTree.CHAR_NAME, TypeTree.CHAR_DSCP);
        SymbolTable.addType(TypeTree.STRING_NAME, TypeTree.STRING_DSCP);
        SymbolTable.addType(TypeTree.VOID_NAME, TypeTree.VOID_DSCP);
        displayList.add(mainSymbolTable);
    }

    private Display() {
    }

    /**
     * add a new level of SymbolTable for Display array
     * @param afterLast if true, the address of new SymbolTable will continue from last address of top SymbolTable.
     *                  It is used for inside a function which have only one local hastype array
     */
    public static void add(boolean afterLast) {
        if (afterLast)
            displayList.add(new SymbolTable(top().getFreeAddress()));
        else
            displayList.add(new SymbolTable());
    }

    public static void add(SymbolTable symbolTable) {
        displayList.add(symbolTable);
    }

    public static SymbolTable pop() {
        return displayList.remove(displayList.size() - 1);
    }

    public static SymbolTable top() {
        return displayList.get(displayList.size() - 1);
    }

    public static Optional<DSCP> find(String name) {
        for (int i = displayList.size() - 1; i >= 0; i--) {
            if (displayList.get(i).contain(name))
                return displayList.get(i).getDSCP(name);
        }
        return Optional.empty();
    }
}
