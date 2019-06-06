package semantic.syntaxTree.expression.binaryOperation.conditional;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.Constants;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;

public class Less extends BinaryOperation {

    public Less(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        setResultType(Constants.INTEGER_DSCP);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(cv, mv);
        getSecondOperand().generateCode(cv, mv);
        Label setFalseLabel = new Label();
        Label outLabel = new Label();
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_1); // false
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        mv.visitLabel(setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_0); // true
        mv.visitLabel(outLabel);
    }
}
