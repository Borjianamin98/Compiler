package semantic.symbolTable.descriptor;

import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.Argument;

import java.util.List;

public class MethodDSCP implements DSCP {
    private String owner;
    private String name;
    private boolean hasReturn;
    private int returnType;
    private List<Argument> arguments;

    public MethodDSCP(String owner, String name, boolean hasReturn, int returnType, List<Argument> arguments) {
        this.owner = owner;
        this.name = name;
        this.hasReturn = hasReturn;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public boolean hasReturn() {
        return hasReturn;
    }

    public int getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public String getDescriptor() {
        return Utility.createMethodDescriptor(arguments, hasReturn, returnType);
    }
}
