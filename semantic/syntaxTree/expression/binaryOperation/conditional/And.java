package semantic.syntaxTree.expression.binaryOperation.conditional;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Constants;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;

public class And extends BinaryOperation {
    public And(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        setResultType(Constants.INTEGER_DSCP);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        Label falseLabel = new Label();
        Label outLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);
        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        mv.visitLabel(falseLabel);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitLabel(outLabel);
    }
}
