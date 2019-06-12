package semantic.syntaxTree.statement.controlflow;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;

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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        if (value != null) {
            value.generateCode(currentClass, currentMethod, cv, mv);
            mv.visitInsn(Utility.getOpcode(value.getResultType(), "RETURN", false));
            if (!currentMethod.hasReturn())
                throw new RuntimeException("Unexpected return type: " + value.getResultType().getName());
            else if (currentMethod.hasReturn() && currentMethod.getReturnType().getTypeCode() != value.getResultType().getTypeCode())
                throw new RuntimeException("Unexpected return type: " + value.getResultType().getName());
        } else {
            if (currentMethod.hasReturn())
                throw new RuntimeException("Unexpected return type: " + currentMethod.getReturnType().getName());
            mv.visitInsn(Opcodes.RETURN);
        }
    }
}
