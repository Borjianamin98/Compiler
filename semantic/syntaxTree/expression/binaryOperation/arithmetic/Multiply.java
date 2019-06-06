package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;

public class Multiply extends BinaryOperation {
    public Multiply(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(cv, mv);
        getSecondOperand().generateCode(cv, mv);
        // TODO check Type (must be completed)
        // TODO Think about char type
        // TODO Think about multiple two strings
        int resultType = getFirstOperand().getResultType();
        mv.visitInsn(Utility.getOpcode(resultType, "MUL"));
    }


}
