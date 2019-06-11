package semantic.syntaxTree.expression.operation.bitwise;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Bitwise extends BinaryOperation {
    private String bitwiseSign;
    private String mainOpcode;
    private TypeDSCP resultType;

    public Bitwise(String bitwiseSign, String mainOpcode, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.bitwiseSign = bitwiseSign;
        this.mainOpcode = mainOpcode;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive() ||
                    !TypeTree.isInteger(getFirstOperand().getResultType()) || !TypeTree.isInteger(getSecondOperand().getResultType()))
                throw new RuntimeException(String.format("Bad operand types for bitwise operator '%s'\n  first type: %s\n  second type: %s",
                        getBitwiseSign(), getFirstOperand().getResultType().getConventionalName(), getSecondOperand().getResultType().getConventionalName()));
            resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
        }
        return resultType;
    }

    public String getBitwiseSign() {
        return bitwiseSign;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getResultType();

        getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

        getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

        mv.visitInsn(Utility.getOpcode(getResultType(), mainOpcode));
    }
}
