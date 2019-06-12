package semantic.symbolTable;

import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.typeTree.TypeTree;

import java.util.*;

public class Display {
    private static List<SymbolTable> displayList = new ArrayList<>();
    static SymbolTable mainSymbolTable = new SymbolTable();

    public static void init() {
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
     *
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

    /**
     * find a symbol in most top symbol table in Display array
     * (most top symbol table shadows other declaration in lowest symbol table)
     *
     * @param name name of symbol
     * @return symbol DSCP if found, otherwise Optional.empty
     */
    public static Optional<DSCP> find(String name) {
        for (int i = displayList.size() - 1; i >= 0; i--) {
            if (displayList.get(i).contain(name))
                return displayList.get(i).getDSCP(name);
        }
        return Optional.empty();
    }

    /**
     * find a a variable and all its dimensions (var, var[], var[][], ...)
     * in most top symbol table in Display array
     * (most top symbol table shadows other declaration in lowest symbol table)
     *
     * @param var name of variable (it can contain dimensions like var[]...[])
     * @return a map from dimension to its variable DSCPs if found, otherwise empty map if
     * symbol not found or symbol is not a variable
     */
    public static Map<Integer, VariableDSCP> findAll(String var) {
        Map<Integer, VariableDSCP> variable = new HashMap<>();

        for (int i = displayList.size() - 1; i >= 0; i--) {
            if (displayList.get(i).getDSCP(var).isPresent()) {
                if (!(displayList.get(i).getDSCP(var).get() instanceof VariableDSCP))
                    continue; // continue to search until find a variable or return empty list

                // this symbol table must contain var and all of its dimensions
                // remove dimension from var
                var = var.replaceAll("\\[\\]", "");

                // get var and all of its dimensions
                Optional<DSCP> fetch;
                while ((fetch = displayList.get(i).getDSCP(var)).isPresent()) {
                    VariableDSCP varDSCP = fetch.map(dscp -> (VariableDSCP) dscp).get();
                    variable.put(varDSCP.getType().getDimensions(), varDSCP);
                    var += "[]";
                }
                break;
            }
        }
        return variable;
    }
}
