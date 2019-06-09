package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class SimpleField extends Variable {
    private HasTypeDSCP dscp;
    private String owner;
    private String name;
    private boolean isStatic;

    public SimpleField(String owner, String name, boolean isStatic) {
        this.owner = owner;
        this.name = name;
        this.isStatic = isStatic;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (!dscp.isInitialized())
            throw new RuntimeException("Field " + name + " of type " + dscp.getType().getName() + " is not initialized");
        if (isStatic) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, name, dscp.getDescriptor());
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, name, dscp.getDescriptor());
        }
        setResultType(dscp.getType());
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (isStatic) {
            value.generateCode(cv, mv);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, name, dscp.getDescriptor());
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
            value.generateCode(cv, mv);
            mv.visitFieldInsn(Opcodes.PUTFIELD, owner, name, dscp.getDescriptor());
        }
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(getName() + " is not declared");
            if (fetchedDSCP.get() instanceof ArrayDSCP || fetchedDSCP.get() instanceof FieldDSCP) {
                dscp = (HasTypeDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(getName() + " is not a field");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }

}
