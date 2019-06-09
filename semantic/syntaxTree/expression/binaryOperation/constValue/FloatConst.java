package semantic.syntaxTree.expression.binaryOperation.constValue;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class FloatConst extends Expression {
    public double value;

    public FloatConst(double value) {
        this.value = value;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.FLOAT_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        mv.visitLdcInsn(new Float(value));
    }
}
