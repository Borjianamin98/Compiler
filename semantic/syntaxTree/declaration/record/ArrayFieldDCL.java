package semantic.syntaxTree.declaration.record;

import exception.DuplicateDeclarationException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Collections;

public class ArrayFieldDCL extends Declaration {
    private String owner;
    private String type;
    private TypeDSCP typeDSCP;
    private int dimensions;
    private TypeDSCP baseTypeDSCP;
    private boolean initialized;
    private boolean beingStatic;

    public ArrayFieldDCL(String owner, String name, String type, int dimensions, boolean isConstant, boolean initialized, boolean beingStatic) {
        super(name, isConstant);
        this.owner = owner;
        this.type = type;
        this.dimensions = dimensions;
        this.initialized = initialized;
        this.beingStatic = beingStatic;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(baseTypeDSCP, dimensions);
    }

    public boolean isStatic() {
        return beingStatic;
    }

    public ArrayTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            baseTypeDSCP = Display.getType(type);
            typeDSCP = Utility.addArrayType(baseTypeDSCP, dimensions);
        }
        return (ArrayTypeDSCP) typeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        SymbolTable top = Display.top();
        if (top.contains(getName()))
            throw new DuplicateDeclarationException(getName());
        if (dimensions <= 0)
            throw new RuntimeException("Filed array declaration must contains at least one dimension");

        getTypeDSCP();
        int access = Opcodes.ACC_PUBLIC;
        access |= isConstant() ? Opcodes.ACC_FINAL : 0;
        access |= isStatic() ? Opcodes.ACC_STATIC : 0;
        cv.visitField(access, getName(), getDescriptor(), null, null).visitEnd();

        // it's type will set after creating all of it's child DSCP
        String finalName = getName() + String.join("", Collections.nCopies(dimensions, "[]"));
        FieldDSCP fieldDSCP = new FieldDSCP(owner, finalName, null, isConstant(), initialized);

        // generate child DSCP
        TypeDSCP lastDimensionType = getTypeDSCP();
        String lastDSCPName = getName();
        for (int i = 0; i <= dimensions - 1; i++) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) lastDimensionType;
            DSCP descriptor = new ArrayDSCP(lastDSCPName, arrayTypeDSCP, arrayTypeDSCP.getInternalType(), baseTypeDSCP,
                    fieldDSCP, i == 0 ? top.getFreeAddress() : -1, false, initialized);
            top.addSymbol(descriptor.getName(), descriptor);
            lastDimensionType = arrayTypeDSCP.getInternalType();
            lastDSCPName = lastDSCPName + "[]";
        }

        fieldDSCP.setType(lastDimensionType);
        top.addSymbol(lastDSCPName, fieldDSCP);
    }

    @Override
    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        if (isConstant())
            represent.append("const ");
        represent.append(Utility.getConvectionalRepresent(type));
        for (int i = 0; i < dimensions; i++) {
            represent.append("[]");
        }
        represent.append(" ").append(getName());
        return represent.toString();
    }
}
