package semantic.syntaxTree.expression.operation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Arithmetic extends BinaryOperation {
    private String arithmeticSign;
    private String mainOpcode;
    protected TypeDSCP resultType;

    public Arithmetic(String arithmeticSign, String mainOpcode, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.arithmeticSign = arithmeticSign;
        this.mainOpcode = mainOpcode;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive() ||
                    TypeTree.isString(getFirstOperand().getResultType()) || TypeTree.isString(getSecondOperand().getResultType()) ||
                    getFirstOperand().getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode() ||
                    getSecondOperand().getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode())
                throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                        getArithmeticSign(), getFirstOperand().getResultType().getConventionalName(), getSecondOperand().getResultType().getConventionalName()));
            resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
        }
        return resultType;
    }

    public String getArithmeticSign() {
        return arithmeticSign;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv, null, null);
        TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv, null, null);
        TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

        mv.visitInsn(Utility.getOpcode(getResultType(), mainOpcode, false));
    }
}
