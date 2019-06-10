package semantic.syntaxTree.expression.binaryoperation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Multiply extends Arithmetic {
    public Multiply(Expression firstOperand, Expression secondOperand) {
        super("*", firstOperand, secondOperand);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // TODO Think about char type
        if (getResultType().getTypeCode() == TypeTree.STRING_DSCP.getTypeCode())
            throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                    getArithmeticSign(), getFirstOperand().getResultType().getName(), getSecondOperand().getResultType().getName()));

        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

        mv.visitInsn(Utility.getOpcode(getResultType().getTypeCode(), "MUL"));
    }


}
