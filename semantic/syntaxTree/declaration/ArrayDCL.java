package semantic.syntaxTree.declaration;

import exception.DuplicateDeclarationException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Collections;

public class ArrayDCL extends Declaration {
    private String type;
    private TypeDSCP typeDSCP;
    private int dimensions;
    private TypeDSCP baseType;
    private boolean initialized;

    public ArrayDCL(String name, String type, int dimensions, boolean isConstant, boolean initialized) {
        super(name, isConstant);
        this.type = type;
        this.dimensions = dimensions;
        this.initialized = initialized;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimensions);
    }

    public ArrayTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            baseType = Display.getType(type);
            typeDSCP = Utility.addArrayType(baseType, dimensions);
        }
        return (ArrayTypeDSCP) typeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        SymbolTable top = Display.top();
        if (top.contains(getName()))
            throw new DuplicateDeclarationException(getName());
        if (dimensions == 0)
            throw new RuntimeException("Array declaration must contains at least one dimension");

        // it's type will set after creating all of it's child DSCP
        String finalName = getName() + String.join("", Collections.nCopies(dimensions, "[]"));
        VariableDSCP variableDSCP = new VariableDSCP(finalName, null, 1, -1, isConstant(), initialized);

        // generate child DSCP
        TypeDSCP lastDimensionType = getTypeDSCP();
        String lastDSCPName = getName();
        for (int i = 0; i <= dimensions - 1; i++) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) lastDimensionType;
            HasTypeDSCP lastDSCP = new ArrayDSCP(lastDSCPName, arrayTypeDSCP, arrayTypeDSCP.getInternalType(), baseType,
                    variableDSCP, i == 0 ? top.getFreeAddress() : -1, false, initialized);
            top.addSymbol(lastDSCP.getName(), lastDSCP);
            lastDimensionType = arrayTypeDSCP.getInternalType();
            lastDSCPName = lastDSCPName + "[]";
        }
        variableDSCP.setType(lastDimensionType);
        top.addSymbol(lastDSCPName, variableDSCP);
    }

    @Override
    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        if (isConstant())
            represent.append("const ");
        represent.append(type);
        for (int i = 0; i < dimensions; i++) {
            represent.append("[]");
        }
        represent.append(" ").append(getName());
        return represent.toString();
    }
}
