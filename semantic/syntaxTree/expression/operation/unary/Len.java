package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.Ignorable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Len extends Expression implements Ignorable {
    private Expression operand;
    private boolean ignoreResult;

    public Len(Expression operand) {
        this.operand = operand;
    }

    @Override
    public SimpleTypeDSCP getResultType() {
        if (!(operand.getResultType() instanceof ArrayTypeDSCP) && !TypeTree.isString(operand.getResultType()))
            throw new RuntimeException("len function can call only on string and array: " + operand.getResultType().getConventionalName());
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getResultType();
        if (operand.getResultType() instanceof ArrayTypeDSCP) {
            operand.generateCode(currentClass, currentMethod, cv, mv, null, null);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
        } else if (TypeTree.isString(operand.getResultType())) {
            operand.generateCode(currentClass, currentMethod, cv, mv, null, null);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
        }
        if (ignoreResult)
            mv.visitInsn(Opcodes.POP);
    }

    @Override
    public void setIgnoreResult(boolean ignoreResult) {
        this.ignoreResult = ignoreResult;
    }

    @Override
    public String getCodeRepresentation() {
        return "len(" + operand.getCodeRepresentation() + ")";
    }
}
