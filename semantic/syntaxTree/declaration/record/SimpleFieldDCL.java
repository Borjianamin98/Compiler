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
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Optional;

public class SimpleFieldDCL extends Declaration {
    private String owner;
    private String type;
    private TypeDSCP typeDSCP;
    private boolean initialized;
    private boolean beingStatic;

    public SimpleFieldDCL(String owner, String name, String type, boolean isConstant, boolean initialized, boolean beingStatic) {
        super(name, isConstant);
        this.owner = owner;
        this.type = type;
        this.initialized = initialized;
        this.beingStatic = beingStatic;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), 0);
    }

    public boolean isStatic() {
        return beingStatic;
    }

    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null)
            typeDSCP = Display.getType(type);
        return typeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contains(getName()))
            throw new DuplicateDeclarationException(getName());

        getTypeDSCP();
        int access = Opcodes.ACC_PUBLIC;
        access |= isConstant() ? Opcodes.ACC_FINAL : 0;
        access |= isStatic() ? Opcodes.ACC_STATIC : 0;
        cv.visitField(access, getName(), getDescriptor(), null, null).visitEnd();
        FieldDSCP fieldDSCP = new FieldDSCP(owner, getName(), getTypeDSCP(), isConstant(), initialized);
        top.addSymbol(getName(), fieldDSCP);
    }

    @Override
    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        if (isConstant())
            represent.append("const ");
        represent.append(Utility.getConvetionalRepresent(type)).append(" ");
        represent.append(getName());
        return represent.toString();
    }
}
