package semantic.syntaxTree.statement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;

public class PrintFunction extends Statement {
    private Expression value;

    /**
     * print value to output stream
     *
     * @param value which is printed
     */
    public PrintFunction(Expression value) {
        this.value = value;
    }

    /**
     * print empty new line
     */
    public PrintFunction() {
        this(null);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        if (value != null) {
            value.generateCode(cv, mv);
            if (value.getResultType().isPrimitive())
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                        "(" + Utility.getPrimitiveTypeDescriptor(value.getResultType().getTypeCode()) + ")V", false);
            else
                throw new RuntimeException("Print a non-primitive value is not possible: " + value.getResultType().getName());
        } else
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "()V", false);
    }
}
