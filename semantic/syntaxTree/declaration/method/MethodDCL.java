package semantic.syntaxTree.declaration.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.VariableDCL;

import java.util.List;
import java.util.Optional;

public class MethodDCL extends Node {
    private String owner;
    private String name;
    private TypeDSCP returnType;
    private Block body;
    private List<Argument> arguments;


    public MethodDCL(String owner, String name, List<Argument> arguments, Block body) {
        this.owner = owner;
        this.name = name;
        this.body = body;
        this.arguments = arguments;
    }

    public MethodDCL(String owner, String name, List<Argument> arguments, Block body, TypeDSCP returnType) {
        this(owner, name, arguments, body);
        this.returnType = returnType;
    }

    public boolean hasReturn() {
        return returnType != null;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescriptor() {
        return Utility.createMethodDescriptor(arguments, hasReturn(), returnType);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        SymbolTable top = Display.top();
        Optional<DSCP> fetchedDSCP = Display.find(name);
        MethodDSCP methodDSCP;
        if (fetchedDSCP.isPresent()) {
            if (!(fetchedDSCP.get() instanceof MethodDSCP))
                throw new DuplicateDeclarationException("Function " + name + " declared more than one time");
            methodDSCP = (MethodDSCP) fetchedDSCP.get();
        } else {
            methodDSCP = new MethodDSCP(owner, name, returnType);
            top.addSymbol(name, methodDSCP);
        }

        methodDSCP.addArguments(arguments);

        // Generate Code
        MethodVisitor methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, name, getDescriptor(), null, null);
        methodVisitor.visitCode();

        // Add function symbol table
        Display.add(false);
        if (arguments != null) {
            for (Argument argument : arguments) {
                Declaration argDCL;
                if (argument.isArray())
                    argDCL = new ArrayDCL(argument.getName(), argument.getBaseType(), argument.getDimensions(), false, true);
                else
                    argDCL = new VariableDCL(argument.getName(), argument.getBaseType(), false, true);
                argDCL.generateCode(cv, mv);
            }
        }

        body.generateCode(cv, methodVisitor);
        Display.pop();

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }
}
