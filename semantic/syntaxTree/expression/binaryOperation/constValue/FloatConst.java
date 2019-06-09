package semantic.syntaxTree.expression.binaryOperation.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Constants;
import semantic.syntaxTree.expression.Expression;

public class FloatConst extends Expression {
    public double value;

    public FloatConst(double value) {
        super(Constants.FLOAT_DSCP);
        this.value = value;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        mv.visitLdcInsn(value);
    }
}
