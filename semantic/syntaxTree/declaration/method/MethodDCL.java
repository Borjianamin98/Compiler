package semantic.syntaxTree.declaration.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class MethodDCL extends Declaration implements ClassCode {
    private String owner;
    private Signature signature;
    private String returnType;
    private TypeDSCP returnTypeDSCP;
    private boolean isStatic;

    public MethodDCL(String owner, Signature signature, String returnType, boolean isStatic) {
        super(signature.getName(), false);
        this.owner = owner;
        this.signature = signature;
        this.isStatic = isStatic;
        this.returnType = returnType;
    }

    public boolean hasReturn() {
        return getReturnType().getTypeCode() != TypeTree.VOID_DSCP.getTypeCode();
    }

    public String getOwner() {
        return owner;
    }

    public String getDescriptor() {
        return Utility.createMethodDescriptor(signature.getArguments(), hasReturn(), getReturnType());
    }

    public TypeDSCP getReturnType() {
        if (returnTypeDSCP == null)
            returnTypeDSCP = Display.getType(returnType);
        return returnTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        SymbolTable top = Display.top();
        Optional<DSCP> fetchedDSCP = Display.find(getName());
        MethodDSCP methodDSCP;
        if (fetchedDSCP.isPresent()) {
            if (!(fetchedDSCP.get() instanceof MethodDSCP))
                throw new RuntimeException(getName() + " declared more than one time");
            methodDSCP = (MethodDSCP) fetchedDSCP.get();
        } else {
            methodDSCP = new MethodDSCP(owner, getName(), getReturnType());
            top.addSymbol(getName(), methodDSCP);
        }

        if (methodDSCP.getReturnType().getTypeCode() != getReturnType().getTypeCode())
            throw new RuntimeException("Overloaded method '" + getName() + "' must have same return type");


        // if declaration have no body, it's mean that is it a method prototype declaration.
        // we added it's descriptor however we doesn't generate code for it.
        // later we see a same method declaration again, if it has body, we complete prototype and
        // generate code for it, otherwise it is a duplicate prototype (if it is same), so we generate error
        if (methodDSCP.contains(signature)) {
            if (methodDSCP.isPrototype(signature)) {
                // Method prototype exists
                if (!signature.hasBody()) {
                    throw new RuntimeException("Prototype of method '" + getName() + "' declared more than once\n" +
                            "arguments: " + signature.getArguments());
                } else {
                    methodDSCP.setDeclared(signature);
                    generate(currentClass, cv);
                }
            } else {
                // Method declared completely
                if (!signature.hasBody()){
                    throw new RuntimeException("Prototype of method '" + getName() + "' after declaration of method");
                } else {
                    throw new RuntimeException("Method '" + getName() + "' with same list of arguments declared more than once\n" +
                            "arguments: " + signature.getArguments());
                }
            }
        } else {
            methodDSCP.addSignature(signature);
            // if it is not a prototype
            if (signature.hasBody())
                generate(currentClass, cv);
        }
    }

    private void generate(ClassDCL currentClass, ClassVisitor cv) {
        // Generate Code
        int access = Opcodes.ACC_PUBLIC;
        access |= isStatic ? Opcodes.ACC_STATIC : 0;
        MethodVisitor methodVisitor = cv.visitMethod(access, getName(), getDescriptor(), null, null);
        methodVisitor.visitCode();

        // Add function symbol table
        Display.add(false);
        if (signature.getArguments() != null) {
            for (Argument argument : signature.getArguments()) {
                Declaration argDCL;
                if (argument.isArray())
                    argDCL = new ArrayDCL(argument.getName(), argument.getBaseType(), argument.getDimensions(), false, true);
                else
                    argDCL = new VariableDCL(argument.getName(), argument.getBaseType(), false, true);
                argDCL.generateCode(currentClass, this, cv, null, null, null);
            }
        }

        // Generate body code
        boolean hasReturnStatement = false;
        if (signature.getBody() != null) {
            for (BlockCode blockCode : signature.getBody().getBlockCodes()) {
                if (hasReturnStatement) // code after return statement is useless
                    throw new RuntimeException("Unreachable statement after return of function");
                if (blockCode instanceof ReturnStatement)
                    hasReturnStatement = true;
                blockCode.generateCode(currentClass, this, cv, methodVisitor, null, null);
            }
        }
        if (!hasReturnStatement)
            throw new RuntimeException("Missing return statement");
        Display.pop();

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }
}
