package semantic.syntaxTree.expression.binaryOperation.conditional;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class And extends BinaryOperation {
    public And(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Label falseLabel = new Label();
        Label outLabel = new Label();

        NotEqual firstCondition = new NotEqual(getFirstOperand(), new IntegerConst(0));
        firstCondition.generateCode(currentClass, currentMethod, cv, mv);
        mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);

        NotEqual secondCondition = new NotEqual(getSecondOperand(), new IntegerConst(0));
        secondCondition.generateCode(currentClass, currentMethod, cv, mv);
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);

        // false result of operation
        mv.visitLabel(falseLabel);
        mv.visitInsn(Opcodes.ICONST_0);

        mv.visitLabel(outLabel);
    }
}
