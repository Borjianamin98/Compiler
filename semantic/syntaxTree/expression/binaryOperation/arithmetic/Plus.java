package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.Constants;
import semantic.exception.TypeMismatchException;
import semantic.symbolTable.Utility;
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
        int resultType = getFirstOperand().getResultType();
        mv.visitInsn(Utility.getOpcode(resultType, "ADD"));
    }


}
