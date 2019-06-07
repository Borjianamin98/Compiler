package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.variable.VariableDSCP;
import semantic.syntaxTree.expression.Expression;

public class VariableDCL extends Declaration {
    private Expression defaultValue;

    public VariableDCL(String name, String type, boolean isConstant, Expression defaultValue) {
        super(name, type, isConstant);
        this.defaultValue = defaultValue;
    }

    public VariableDCL(String name, String type, boolean isConstant) {
        this(name, type, isConstant, null);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        SymbolTable top = Display.top();
        boolean isInitialized = false;
        if (getTypeDSCP() instanceof RecordTypeDSCP) {
            RecordTypeDSCP recordTypeDSCP = (RecordTypeDSCP) getTypeDSCP();
            mv.visitTypeInsn(Opcodes.NEW, recordTypeDSCP.getName());
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, recordTypeDSCP.getName(), "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, top.getFreeAddress());
            isInitialized = true;
        }
        if (defaultValue != null) {
            defaultValue.generateCode(cv, mv);
            mv.visitVarInsn(Utility.getOpcode(defaultValue.getResultType().getTypeCode(), "STORE"), top.getFreeAddress());
            isInitialized = true;
        }
        VariableDSCP variableDSCP = new VariableDSCP(getName(), getTypeDSCP(), getTypeDSCP().getSize(), top.getFreeAddress(), isConstant(), isInitialized);
        top.addSymbol(getName(), variableDSCP);
    }
}
