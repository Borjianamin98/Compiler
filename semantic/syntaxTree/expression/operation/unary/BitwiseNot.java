package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        operand.generateCode(currentClass, currentMethod, cv, mv);
        Utility.getSimpleConstant(getResultType(), -1).generateCode(currentClass, currentMethod, cv, mv);
        mv.visitInsn(Utility.getOpcode(getResultType(), "XOR", false));
    }
}
