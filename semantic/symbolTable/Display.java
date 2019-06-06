package semantic.symbolTable;

import semantic.symbolTable.descriptor.DSCP;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Display {
    private static List<SymbolTable> displayList = new ArrayList<>();

    private Display() {
    }

    /**
     * add a new level of SymbolTable for Display array
     * @param afterLast if true, the address of new SymbolTable will continue from last address of top SymbolTable.
     *                  It is used for inside a function which have only one local variable array
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

    public static void pop() {
        displayList.remove(displayList.size() - 1);
    }

    public static SymbolTable top() {
        return displayList.get(displayList.size() - 1);
    }

    public static Optional<DSCP> find(String name) {
        for (int i = displayList.size() - 1; i >= 0; i--) {
            if (displayList.get(i).contain(name))
                return Optional.of(displayList.get(i).getDSCP(name));
        }
        return Optional.empty();
    }
}
