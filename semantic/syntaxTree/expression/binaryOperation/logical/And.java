package semantic.syntaxTree.expression.binaryOperation.logical;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

public class And extends Logical {
    public And(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Label falseLabel = new Label();
        Label outLabel = new Label();

        evaluateBoolean(currentClass, currentMethod, cv, mv, getFirstOperand(), falseLabel);

        evaluateBoolean(currentClass, currentMethod, cv, mv, getSecondOperand(), falseLabel);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);

        // false result of operation
        mv.visitLabel(falseLabel);
        mv.visitInsn(Opcodes.ICONST_0);

        mv.visitLabel(outLabel);
    }
}
