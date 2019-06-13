package semantic.syntaxTree.expression.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;

public class LongConst extends Expression {
    public long value;

    public LongConst(long value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.LONG_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (value == 0 || value == 1)
            mv.visitInsn(Utility.getOpcode("L", "CONST", "_" + value));
        else
            mv.visitLdcInsn(value);
    }

    @Override
    public String getCodeRepresentation() {
        return String.valueOf(value);
    }
}
