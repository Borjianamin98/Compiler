package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Neg extends Expression {
    private Expression operand;
    private TypeDSCP resultType;

    public Neg(Expression operand) {
        this.operand = operand;
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!operand.getResultType().isPrimitive() ||
                    TypeTree.isString(operand.getResultType()) ||
                    operand.getResultType().getTypeCode() == TypeTree.VOID_DSCP.getTypeCode())
                throw new RuntimeException(String.format("Bad operand types for unary minus operator: %s",
                        operand.getResultType().getConventionalName()));
            resultType = operand.getResultType();
        }
        return resultType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        operand.generateCode(currentClass, currentMethod, cv, mv, null, null);
        mv.visitInsn(Utility.getOpcode(getResultType(), "NEG", false));
    }
}
