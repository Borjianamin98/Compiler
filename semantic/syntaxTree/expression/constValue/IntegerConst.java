package semantic.syntaxTree.expression.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class IntegerConst extends Expression {
    public int value;

    public IntegerConst(int value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (value == -1)
            mv.visitInsn(Opcodes.ICONST_M1);
        else if (value >= 0 && value <= 5)
            mv.visitInsn(Utility.getOpcode("I", "CONST", "_" + value));
        else
            mv.visitLdcInsn(value);
    }
}
