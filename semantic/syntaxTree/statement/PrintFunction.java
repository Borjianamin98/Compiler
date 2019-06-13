package semantic.syntaxTree.statement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        if (value != null) {
            value.generateCode(currentClass, currentMethod, cv, mv, null, null);
            if (!value.getResultType().isPrimitive() ||
                    TypeTree.isVoid(value.getResultType()))
                throw new RuntimeException("Print a non-primitive value is not possible: " + value.getResultType().getConventionalName());
            else
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                        "(" + Utility.getPrimitiveTypeName(value.getResultType()) + ")V", false);
        } else
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "()V", false);
    }

    public String getCodeRepresentation() {
        return "println(" + value.getCodeRepresentation() + ")";
    }
}
