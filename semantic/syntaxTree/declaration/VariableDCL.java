package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.RecordTypeDSCP;
import semantic.symbolTable.descriptor.TypeDSCP;
import semantic.symbolTable.descriptor.VariableDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class VariableDCL extends Declaration {

    public VariableDCL(String name, String type, boolean isConstant, Expression defaultValue) {
        super(name, type, isConstant, defaultValue);
    }

    public VariableDCL(String name, String type, boolean isConstant) {
        super(name, type, isConstant, null);
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
        if (getDefaultValue() != null) {
            getDefaultValue().generateCode(cv, mv);
            mv.visitVarInsn(Utility.getOpcode(getDefaultValue().getResultType().getTypeCode(), "STORE"), top.getFreeAddress());
            isInitialized = true;
        }
        VariableDSCP variableDSCP = new VariableDSCP(getName(), getTypeDSCP(), getTypeDSCP().getSize(), top.getFreeAddress(), isConstant(), isInitialized);
        top.addSymbol(getName(), variableDSCP);
    }
}
