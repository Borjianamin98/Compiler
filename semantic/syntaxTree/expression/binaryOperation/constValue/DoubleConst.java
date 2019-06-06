package semantic.syntaxTree.expression.binaryOperation.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.Constants;
import semantic.syntaxTree.expression.Expression;

public class DoubleConst extends Expression {
    public double value;

    public DoubleConst(double value) {
        super(Constants.DOUBLE_DSCP);
        this.value = value;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        mv.visitLdcInsn(value);
    }
}
