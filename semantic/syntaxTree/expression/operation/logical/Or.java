package semantic.syntaxTree.expression.operation.logical;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

public class Or extends Logical {
    public Or(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        Label trueLabel = new Label();
        Label outLabel = new Label();

        Utility.evaluateBooleanExpressionTrue(currentClass, currentMethod, cv, mv, getFirstOperand(), trueLabel);

        Utility.evaluateBooleanExpressionTrue(currentClass, currentMethod, cv, mv, getSecondOperand(), trueLabel);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);

        // true result of operation
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(outLabel);
    }
}
