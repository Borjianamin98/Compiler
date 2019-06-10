package semantic.syntaxTree.declaration.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MethodDCL extends Declaration {
    private String owner;
    private TypeDSCP returnType;
    private Block body;
    private List<Argument> arguments;
    private boolean isStatic;

    public MethodDCL(String owner, String name, List<Argument> arguments, Block body, boolean isStatic) {
        super(name, false);
        this.owner = owner;
        this.body = body;
        this.arguments = arguments;
        this.isStatic = isStatic;
    }

    public MethodDCL(String owner, String name, List<Argument> arguments, Block body, TypeDSCP returnType, boolean isStatic) {
        this(owner, name, arguments, body, isStatic);
        this.returnType = returnType;
    }

    public boolean hasReturn() {
        return returnType != null;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescriptor() {
        return Utility.createMethodDescriptor(arguments, hasReturn(), returnType);
    }

    public TypeDSCP getReturnType() {
        return returnType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        SymbolTable top = Display.top();
        Optional<DSCP> fetchedDSCP = Display.find(getName());
        MethodDSCP methodDSCP;
        if (fetchedDSCP.isPresent()) {
            if (!(fetchedDSCP.get() instanceof MethodDSCP))
                throw new DuplicateDeclarationException("Function " + getName() + " declared more than one time");
            methodDSCP = (MethodDSCP) fetchedDSCP.get();
        } else {
            methodDSCP = new MethodDSCP(owner, getName(), returnType);
            top.addSymbol(getName(), methodDSCP);
        }

        methodDSCP.addArguments(arguments == null ? new ArrayList<>() : arguments);

        // Generate Code
        int access = Opcodes.ACC_PUBLIC;
        access |= isStatic ? Opcodes.ACC_STATIC : 0;
        MethodVisitor methodVisitor = cv.visitMethod(access, getName(), getDescriptor(), null, null);
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
                argDCL.generateCode(currentClass, this, cv, mv);
            }
        }

        // Generate body code
        boolean hasReturnStatement = false;
        if (body != null) {
            for (BlockCode blockCode : body.getBlockCodes()) {
                if (blockCode instanceof BreakStatement)
                    throw new RuntimeException("Break outside of loop");
                else if (blockCode instanceof ContinueStatement)
                    throw new RuntimeException("Continue outside of loop");
                else {
                    if (hasReturnStatement) // code after return statement is useless
                        throw new RuntimeException("Unreachable statement after return of function");
                    if (blockCode instanceof ReturnStatement)
                        hasReturnStatement = true;
                    blockCode.generateCode(currentClass, this, cv, methodVisitor);
                }
            }
        }
        if (!hasReturnStatement)
            throw new RuntimeException("Missing return statement");
        Display.pop();

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }
}
