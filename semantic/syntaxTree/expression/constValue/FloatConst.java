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

public class FloatConst extends Expression {
    public float value;

    public FloatConst(float value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.FLOAT_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (Float.compare(value, 0) == 0 || Float.compare(value, 1) == 0 || Float.compare(value, 2) == 0)
            mv.visitInsn(Utility.getOpcode("F", "CONST", "_" + (int)value));
        else
            mv.visitLdcInsn(value);
    }

    @Override
    public String getCodeRepresentation() {
        return String.valueOf(value);
    }
}
