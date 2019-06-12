package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class SimpleFieldVariable extends Variable {
    private HasTypeDSCP dscp;
    private String name;
    private boolean isStatic;

    public SimpleFieldVariable(String name, boolean isStatic) {
        super(name);
        this.name = name;
        this.isStatic = isStatic;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getDSCP();
        if (!getDSCP().isInitialized())
            throw new RuntimeException(String.format("Variable %s might not have been initialized", getChainName()));
        if (isStatic) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, currentClass.getName(), name, dscp.getDescriptor());
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
            mv.visitFieldInsn(Opcodes.GETFIELD, currentClass.getName(), name, dscp.getDescriptor());
        }
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (getDSCP().isConstant())
            throw new RuntimeException(String.format("Cannot assign a value to const variable %s. Variable %s already have been assigned",
                    getChainName(), getChainName()));
        if (isStatic) {
            value.generateCode(currentClass, currentMethod, cv, mv, null, null);
            TypeTree.widen(mv, getResultType(), value.getResultType()); // right value must be converted to type of variable
            mv.visitFieldInsn(Opcodes.PUTSTATIC, currentClass.getName(), name, dscp.getDescriptor());
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
            value.generateCode(currentClass, currentMethod, cv, mv, null, null);
            TypeTree.widen(mv, value.getResultType(), getResultType()); // right value must be converted to type of variable
            mv.visitFieldInsn(Opcodes.PUTFIELD, currentClass.getName(), name, dscp.getDescriptor());
        }
        getDSCP().setInitialized(true);
        setInitializationOfArray(value);
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new RuntimeException(getName() + " is not declared");
            if (fetchedDSCP.get() instanceof ArrayDSCP || fetchedDSCP.get() instanceof FieldDSCP) {
                dscp = (HasTypeDSCP) fetchedDSCP.get();
            } else
                throw new RuntimeException(getName() + " is not a field");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }

}
