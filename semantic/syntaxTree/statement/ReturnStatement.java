package semantic.syntaxTree.statement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;

public class ReturnStatement extends Statement {
    private Expression value;

    /**
     * return statement which return value expression
     *
     * @param value of return statement
     */
    public ReturnStatement(Expression value) {
        this.value = value;
    }

    /**
     * empty return statement
     */
    public ReturnStatement() {
        this(null);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (value != null) {
            value.generateCode(cv, mv);
            mv.visitInsn(Utility.getOpcode(value.getResultType().getTypeCode(), "RETURN"));
        } else
            mv.visitInsn(Opcodes.RETURN);
    }
}
