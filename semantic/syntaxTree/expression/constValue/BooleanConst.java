package semantic.syntaxTree.expression.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;

public class BooleanConst extends Expression {
    public boolean value;

    public BooleanConst(boolean value) {
        this.value = value;
    }

    public BooleanConst(int value) {
        this.value = value == 1;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.BOOLEAN_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (value)
            mv.visitInsn(Opcodes.ICONST_1);
        else
            mv.visitInsn(Opcodes.ICONST_0);
    }

    @Override
    public String getCodeRepresentation() {
        return String.valueOf(value);
    }
}
