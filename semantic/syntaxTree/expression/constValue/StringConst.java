package semantic.syntaxTree.expression.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class StringConst extends Expression {
    public String value;

    public StringConst(String value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.STRING_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        mv.visitLdcInsn(value);
    }
}
