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
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.block.Block;

import java.util.List;

public class MethodDCL extends Node {
    private String owner;
    private String name;
    private boolean hasReturn;
    private TypeDSCP returnType;
    private Block body;
    private List<Argument> arguments;


    public MethodDCL(String owner, String name, List<Argument> arguments, Block body) {
        this.owner = owner;
        this.name = name;
        this.body = body;
        this.arguments = arguments;
        this.hasReturn = false;
    }

    public MethodDCL(String owner, String name, List<Argument> arguments, Block body, TypeDSCP returnType) {
        this(owner, name, arguments, body);
        this.returnType = returnType;
        this.hasReturn = true;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescriptor() {
        return Utility.createMethodDescriptor(arguments, hasReturn, returnType);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        // Update SymbolTable
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(name)) {
            throw new DuplicateDeclarationException(name + " declared more than one time");
        }
        MethodDSCP methodDSCP = new MethodDSCP(owner, name, hasReturn, returnType, arguments);
        top.addSymbol(name, methodDSCP);

        // Generate Code
        MethodVisitor methodVisitor = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, name, getDescriptor(), null, null);
        methodVisitor.visitCode();

        SymbolTable currentFunctionSYMTAB = new SymbolTable();
        if (arguments != null) {
            for (Argument argument : arguments) {
                int freeAddress = currentFunctionSYMTAB.getFreeAddress();

                TypeDSCP lastDimensionType = argument.getOriginType();
                for (int i = argument.getArrayLevels() - 1; i >= 0; i--) {
                    TypeDSCP typeDSCP;
                    if ((typeDSCP = SymbolTable.getType("[" + lastDimensionType.getDescriptor())) == null) {
                        typeDSCP = new ArrayTypeDSCP(lastDimensionType, argument.getOriginType());
                        SymbolTable.addType(typeDSCP.getName(), typeDSCP);
                    }
                    DSCP descriptor;
                    if (i == 0) {
                        // TODO Think about initialization value
                        descriptor = new VariableDSCP(argument.getName() + "$" + i, typeDSCP, 1, freeAddress, false, true);
                    } else {
                        descriptor = new ArrayDSCP(argument.getName() + "$" + i, typeDSCP, lastDimensionType, argument.getOriginType(), false, true);
                    }
                    currentFunctionSYMTAB.addSymbol(descriptor.getName(), descriptor);
                    lastDimensionType = typeDSCP;
                }
            }
        }
        Display.add(currentFunctionSYMTAB);
        body.generateCode(cv, methodVisitor);
        Display.pop();

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }
}
