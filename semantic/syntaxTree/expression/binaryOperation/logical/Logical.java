package semantic.syntaxTree.expression.binaryOperation.logical;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public abstract class Logical extends BinaryOperation {

    public Logical(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    protected void evaluateBoolean(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                                   Expression operand, Label falseLabel) {
        TypeDSCP operandType = operand.getResultType();
        operand.generateCode(currentClass, currentMethod, cv, mv);
        if (operandType.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode()) {
            mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
        } else if (operandType.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.LCONST_0);
            mv.visitInsn(Opcodes.DCMPL);
            mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
        } else if (operandType.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.FCONST_0);
            mv.visitInsn(Opcodes.FCMPL);
            mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
        } else if (operandType.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitInsn(Opcodes.DCMPL);
            mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
        } else if (operandType.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false);
            mv.visitJumpInsn(Opcodes.IFNE, falseLabel);
        }
    }
}
