package semantic.symbolTable;

import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.typeTree.TypeTree;

import java.util.*;

public class Display {
    private static List<SymbolTable> displayList = new ArrayList<>();
    static SymbolTable mainSymbolTable = new SymbolTable();
    private static int freeType;

    public static void init() {
        addType(TypeTree.INTEGER_NAME, TypeTree.INTEGER_DSCP);
        addType(TypeTree.BOOLEAN_NAME, TypeTree.BOOLEAN_DSCP);
        addType(TypeTree.LONG_NAME, TypeTree.LONG_DSCP);
        addType(TypeTree.FLOAT_NAME, TypeTree.FLOAT_DSCP);
        addType(TypeTree.DOUBLE_NAME, TypeTree.DOUBLE_DSCP);
        addType(TypeTree.CHAR_NAME, TypeTree.CHAR_DSCP);
        addType(TypeTree.STRING_NAME, TypeTree.STRING_DSCP);
        addType(TypeTree.VOID_NAME, TypeTree.VOID_DSCP);
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
     * all of type (integer, long, ... and array or record type are add to
     * main symbol table in Display. Assign a unique id to each type which is
     * added to main symbol table
     *
     * @param name       name of type
     * @param descriptor TypeDSCP of type
     * @throws RuntimeException if type declared more than one time
     */
    public static void addType(String name, TypeDSCP descriptor) {
        if (Display.mainSymbolTable.contains(name))
            throw new RuntimeException("Type '" + name + "' declared more than once");

        descriptor.setTypeCode(freeType);
        freeType++;
        Display.mainSymbolTable.getSymbols().put(name, descriptor);
    }

    /**
     * get a TypeDSCP of a type
     *
     * @param name name of type
     * @return TypeDSCP of type
     * @throws RuntimeException if type not found or name is not a type
     */
    public static TypeDSCP getType(String name) {
        if (!Display.mainSymbolTable.contains(name))
            throw new RuntimeException("Type '" + name + "' is not declared");
        DSCP dscp = Display.mainSymbolTable.getSymbols().get(name);
        if (!(dscp instanceof TypeDSCP))
            throw new RuntimeException("'" + name + "' is not a type");
        return (TypeDSCP) dscp;
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
            if (displayList.get(i).contains(name))
                return displayList.get(i).getDSCP(name);
        }
        return Optional.empty();
    }

    /**
     * find a symbol in most top symbol table in Display array
     * (most top symbol table shadows other declaration in lowest symbol table)
     *
     * @param name name of symbol
     * @return SymbolTable of variable if found, otherwise Optional.empty
     */
    public static Optional<SymbolTable> findSymbolTable(String name) {
        for (int i = displayList.size() - 1; i >= 0; i--) {
            if (displayList.get(i).contains(name))
                return Optional.ofNullable(displayList.get(i));
        }
        return Optional.empty();
    }

    /**
     * find a a variable and all its dimensions (var, var[], var[][], ...) in symbolTable given to function
     *
     * @param symbolTable symbol table to search for var
     * @param var         name of variable (it can contains dimensions like var[]...[])
     * @return a map from dimension to its variable DSCPs if found, otherwise empty map if
     * symbol not found or symbol is not a variable
     */
    public static Map<Integer, HasTypeDSCP> findAll(SymbolTable symbolTable, String var) {
        Map<Integer, HasTypeDSCP> variable = new HashMap<>();

        if (symbolTable.getDSCP(var).isPresent()) {
            if (!(symbolTable.getDSCP(var).get() instanceof VariableDSCP))
                return variable; // return empty list

            // this symbol table must contains var and all of its dimensions
            // remove dimension from var
            var = var.replaceAll("\\[\\]", "");

            // get var and all of its dimensions
            Optional<DSCP> fetch;
            while ((fetch = symbolTable.getDSCP(var)).isPresent()) {
                HasTypeDSCP varDSCP = fetch.map(dscp -> (HasTypeDSCP) dscp).get();
                variable.put(varDSCP.getType().getDimensions(), varDSCP);
                var += "[]";
            }
        }
        return variable;
    }
}
