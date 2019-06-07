package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;

public class Plus extends BinaryOperation {
    public Plus(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(cv, mv);
        getSecondOperand().generateCode(cv, mv);
        // TODO check Type (must be completed)
        // TODO Think about char type
        // TODO Think about adding two strings
        TypeDSCP resultType = getFirstOperand().getResultType();
        mv.visitInsn(Utility.getOpcode(resultType.getTypeCode(), "ADD"));
    }


}
