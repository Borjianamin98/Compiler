package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;

public class Multiply extends BinaryOperation {
    public Multiply(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        // TODO check Type (must be completed)
        // TODO Think about char type
        // TODO Think about multiple two strings
        TypeDSCP resultType = getFirstOperand().getResultType();
        mv.visitInsn(Utility.getOpcode(resultType.getTypeCode(), "MUL"));
    }


}
