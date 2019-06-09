package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.Declaration;

import java.util.Optional;

public class SimpleFieldDCL extends Declaration {
    private String owner;
    private boolean initialized;
    private boolean beingStatic;

    public SimpleFieldDCL(String owner, String name, String type, boolean isConstant, boolean initialized, boolean beingStatic) {
        super(name, type, isConstant);
        this.owner = owner;
        this.initialized = initialized;
        this.beingStatic = beingStatic;
    }

    public String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), 0);
    }

    public boolean isStatic() {
        return beingStatic;
    }

    @Override
    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getType());
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException("Type " + getType() + " not found");
            typeDSCP = (TypeDSCP) fetchedDSCP.get();
        }
        return typeDSCP;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(getName()))
            throw new DuplicateDeclarationException("Field " + getName() + " declared more than one time");

        getTypeDSCP();
        int access = Opcodes.ACC_PUBLIC;
        access |= isConstant() ? Opcodes.ACC_FINAL : 0;
        access |= isStatic() ? Opcodes.ACC_STATIC : 0;
        cv.visitField(access, getName(), getDescriptor(), null, null).visitEnd();
        FieldDSCP fieldDSCP = new FieldDSCP(owner, getName(), getTypeDSCP(), isConstant(), initialized);
        top.addSymbol(getName(), fieldDSCP);
    }
}