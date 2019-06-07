package semantic.syntaxTree.expression.binaryOperation.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.Constants;
import semantic.syntaxTree.expression.Expression;

public class StringConst extends Expression {
    public String value;

    public StringConst(String value) {
        super(Constants.STRING_DSCP);
        this.value = value;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        mv.visitLdcInsn(value);
    }
}
