package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;

public class BitwiseNot extends Expression {
    private Expression operand;
    private TypeDSCP resultType;

    public BitwiseNot(Expression operand) {
        this.operand = operand;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!operand.getResultType().isPrimitive() ||
                    !TypeTree.isInteger(operand.getResultType()))
                throw new RuntimeException(String.format("Bad operand types for unary bitwise not operator: %s",
                        operand.getResultType().getConventionalName()));
            resultType = operand.getResultType();
        }
        return resultType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        operand.generateCode(currentClass, currentMethod, cv, mv, null, null);
        Utility.getSimpleConstant(getResultType(), -1).generateCode(currentClass, currentMethod, cv, mv, null, null);
        mv.visitInsn(Utility.getOpcode(getResultType(), "XOR", false));
    }

    @Override
    public String getCodeRepresentation() {
        return "~(" + operand.getCodeRepresentation() + ")";
    }
}
