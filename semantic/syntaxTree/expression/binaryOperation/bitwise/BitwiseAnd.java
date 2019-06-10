package semantic.syntaxTree.expression.binaryOperation.bitwise;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryOperation.arithmetic.Arithmetic;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class BitwiseAnd extends Bitwise {
    public BitwiseAnd(Expression firstOperand, Expression secondOperand) {
        super("&", firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getResultType();

        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

        mv.visitInsn(Utility.getOpcode(getResultType().getTypeCode(), "AND"));
    }
}
