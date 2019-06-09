package semantic.symbolTable.descriptor;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.Argument;

import java.util.ArrayList;
import java.util.List;

public class MethodDSCP extends DSCP {
    private String owner;
    private String name;
    private TypeDSCP returnType;
    /**
     * contain a list of arguments for each method overloading
     */
    private List<List<Argument>> arguments;

    public MethodDSCP(String owner, String name, TypeDSCP returnType) {
        super(name);
        this.owner = owner;
        this.name = name;
        this.returnType = returnType;
        arguments = new ArrayList<>();
    }

    public void addArguments(List<Argument> newArgument) {
        for (List<Argument> argument : arguments) {
            if (argument.equals(newArgument))
                throw new RuntimeException("Method overloaded with same arguments as another overloading");
        }
        arguments.add(newArgument);
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public boolean hasReturn() {
        return returnType != null;
    }

    public TypeDSCP getReturnType() {
        return returnType;
    }

    public String getDescriptor(int indexOfOverloadedMethod) {
        if (indexOfOverloadedMethod < 0 || indexOfOverloadedMethod >= arguments.size())
            throw new RuntimeException("Can't find method");
        return Utility.createMethodDescriptor(arguments.get(indexOfOverloadedMethod), hasReturn(), returnType);
    }
}
