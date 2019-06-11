package semantic.syntaxTree.expression.operation.arithmetic;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Plus extends Arithmetic {
    public Plus(Expression firstOperand, Expression secondOperand) {
        super("+", "ADD", firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        if (resultType == null) {
            if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive())
                throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                        getArithmeticSign(), getFirstOperand().getResultType().getConventionalName(), getSecondOperand().getResultType().getConventionalName()));
            // handle special case which one of operand is string
            if (getFirstOperand().getResultType().getTypeCode() == TypeTree.STRING_DSCP.getTypeCode() ||
                    getSecondOperand().getResultType().getTypeCode() == TypeTree.STRING_DSCP.getTypeCode())
                resultType = TypeTree.STRING_DSCP;
            else
                resultType = TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
        }
        return resultType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        if (getResultType().getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
            /**
             * firstOperand:String + secondOperand:String
             * converted to
             * new StringBuilder().append(firstOperand).append(secondOperand).toString();
             */
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" +
                    Utility.getDescriptor(getFirstOperand().getResultType(), 0) + ")Ljava/lang/StringBuilder;", false);

            getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" +
                    Utility.getDescriptor(getSecondOperand().getResultType(), 0) + ")Ljava/lang/StringBuilder;", false);

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        } else {
            super.generateCode(currentClass, currentMethod, cv, mv);
        }
    }
}
