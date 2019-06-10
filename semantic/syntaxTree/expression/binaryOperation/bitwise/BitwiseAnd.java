package semantic.syntaxTree.expression.binaryoperation.bitwise;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
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
