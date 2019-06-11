package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class Len extends Expression {
    private Expression operand;

    public Len(Expression operand) {
        this.operand = operand;
    }

    @Override
    public SimpleTypeDSCP getResultType() {
        if (!(operand.getResultType() instanceof ArrayTypeDSCP) || !TypeTree.isString(operand.getResultType()))
            throw new RuntimeException("len function can call only on string and array");
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getResultType();
        if (operand.getResultType() instanceof ArrayTypeDSCP) {
            operand.generateCode(currentClass, currentMethod, cv, mv);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
        } else if (TypeTree.isString(operand.getResultType())) {
            operand.generateCode(currentClass, currentMethod, cv, mv);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
        }
    }
}
