package semantic.syntaxTree.expression.binaryOperation.conditional;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Less extends BinaryOperation {

    public Less(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        Label setFalseLabel = new Label();
        Label outLabel = new Label();
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_1); // false
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        mv.visitLabel(setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_0); // true
        mv.visitLabel(outLabel);
    }
}
