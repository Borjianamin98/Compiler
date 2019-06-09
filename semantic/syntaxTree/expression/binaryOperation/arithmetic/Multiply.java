package semantic.syntaxTree.expression.binaryOperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Multiply extends BinaryOperation {
    public Multiply(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // TODO check Type (must be completed)
        // TODO Think about char type
        // TODO Think about multiple two strings
        getResultType();

        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

        mv.visitInsn(Utility.getOpcode(getResultType().getTypeCode(), "MUL"));
    }


}
