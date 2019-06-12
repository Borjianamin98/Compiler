package semantic.syntaxTree.expression.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class DoubleConst extends Expression {
    public double value;

    public DoubleConst(double value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.DOUBLE_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (Double.compare(value, 0) == 0 || Double.compare(value, 1) == 0)
            mv.visitInsn(Utility.getOpcode("D", "CONST", "_" + (int)value));
        else
            mv.visitLdcInsn(value);
    }
}
