package semantic.syntaxTree.expression.operation.relational;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Equal extends Relational {

    public Equal(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        TypeDSCP compareType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());

        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getFirstOperand().getResultType(), compareType);

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getSecondOperand().getResultType(), compareType);

        Label setFalseLabel = new Label();
        Label outLabel = new Label();

        if (compareType.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode())
            mv.visitJumpInsn(Opcodes.IF_ICMPNE, setFalseLabel);
        else if (compareType.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.LCMP);
            mv.visitJumpInsn(Opcodes.IFNE, setFalseLabel);
        } else if (compareType.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.FCMPL);
            mv.visitJumpInsn(Opcodes.IFNE, setFalseLabel);
        } else if (compareType.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            mv.visitInsn(Opcodes.DCMPL);
            mv.visitJumpInsn(Opcodes.IFGE, setFalseLabel);
        } else if (compareType.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "compareTo", "(Ljava/lang/String;)I", false);
            mv.visitJumpInsn(Opcodes.IFNE, setFalseLabel);
        }

        mv.visitInsn(Opcodes.ICONST_1); // false
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        mv.visitLabel(setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_0); // true
        mv.visitLabel(outLabel);
    }
}
