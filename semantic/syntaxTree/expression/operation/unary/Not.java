package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;

public class Not extends Expression {
    private Expression operand;

    public Not(Expression operand) {
        this.operand = operand;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        Label setFalseLabel = new Label();
        Label outLabel = new Label();

        Utility.evaluateBooleanExpressionTrue(currentClass, currentMethod, cv, mv, operand, setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);

        // false result of operation
        mv.visitLabel(setFalseLabel);
        mv.visitInsn(Opcodes.ICONST_0);

        mv.visitLabel(outLabel);
    }

    @Override
    public String getCodeRepresentation() {
        return "not(" + operand.getCodeRepresentation() + ")";
    }
}
