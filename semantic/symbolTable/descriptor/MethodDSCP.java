package semantic.symbolTable.descriptor;

import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.Argument;
import semantic.syntaxTree.declaration.method.Signature;
import semantic.symbolTable.typeTree.TypeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodDSCP extends DSCP {
    private String owner;
    private String name;
    private TypeDSCP returnType;
    /**
     * a list of signatures of function
     * signature.hasBody() show that whether for a signature, declaration is
     * provided, or it is only a prototype
     */
    private List<Signature> signatures;

    public MethodDSCP(String owner, String name, TypeDSCP returnType) {
        super(name);
        this.owner = owner;
        this.name = name;
        this.returnType = returnType;
        signatures = new ArrayList<>();
    }

    /**
     * @throws RuntimeException if any of overloaded function are not defined (is prototype after analyze all of class)
     */
    public void checkAllSignaturesDeclared() {
        for (Signature signature : signatures) {
            if (!signature.hasBody())
                throw new RuntimeException("Method has a prototype without any declaration: " + signature);
        }
    }

    /**
     * add a sginature to a method
     * @param signature a given signature
     * @throws RuntimeException if given signature exists from before (whether prototype or declaration)
     */
    public void addSignature(Signature signature) {
        if (contains(signature))
            throw new RuntimeException("Method overloaded with same signature");
        signatures.add(signature);
    }

    /**
     * check whether function with a given signature exists.
     * @param signature a signature of method
     * @return true if function contains a signature like given signature
     *              (it is not important whether it is a declaration or a prototype)
     */
    public boolean contains(Signature signature) {
        return signatures.contains(signature);
    }

    /**
     * check whether function with a given signature is declared as prototype
     * @param signature a signature
     * @return true if it is a prototype and doesn't contain body
     */
    public boolean isPrototype(Signature signature) {
        return !find(signature).hasBody();
    }

    /**
     * set function to declared (it means that it has body)
     * it is not prototype anymore
     * remove last one and add new signature instead of that
     * @param signature a list of argument
     * @throws RuntimeException if method doesn't have any signature like input of method
     */
    public void setDeclared(Signature signature) {
        find(signature);
        signatures.remove(find(signature));
        signatures.add(signature);
    }

    /**
     * find a signature which is equals to sgn
     * look at equals of signature for more information
     * @param sgn signature
     * @return a signature which equals to sgn
     * @throws RuntimeException if doesn't find any match
     */
    public Signature find(Signature sgn) {
        List<Signature> collect = signatures.stream().filter(sgn::equals).collect(Collectors.toList());
        if (collect.isEmpty())
            throw new RuntimeException("There is no method with this signature: " + sgn);
        if (collect.size() > 1)
            throw new AssertionError("doesn't happen");
        return collect.get(0);
    }

    /**
     * all of method overloading which len(arguments) == argumentLength
     * @param argumentLength argument length
     * @return a list of list of arguments
     */
    public List<List<Argument>> getAllArguments(int argumentLength) {
        return signatures.stream().filter(sgn -> sgn.getArguments().size() == argumentLength)
                .map(Signature::getArguments)
                .collect(Collectors.toList());
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public boolean hasReturn() {
        return returnType.getTypeCode() != TypeTree.VOID_DSCP.getTypeCode();
    }

    public TypeDSCP getReturnType() {
        return returnType;
    }

    public String getDescriptor(List<Argument> arguments) {
        Signature tempSignature = new Signature(name, arguments, null);
        if (!contains(tempSignature))
            throw new RuntimeException("There is no method with this signature: " + tempSignature);
        return Utility.createMethodDescriptor(arguments, hasReturn(), returnType);
    }

}
