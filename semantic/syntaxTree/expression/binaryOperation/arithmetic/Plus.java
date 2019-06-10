package semantic.syntaxTree.expression.binaryOperation.arithmetic;

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
        super("+", firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        if (!getFirstOperand().getResultType().isPrimitive() || !getSecondOperand().getResultType().isPrimitive())
            throw new RuntimeException(String.format("Bad operand types for binary operator '%s'\n  first type: %s\n  second type: %s",
                    getArithmeticSign(), getFirstOperand().getResultType().getName(), getSecondOperand().getResultType().getName()));
        return TypeTree.max(getFirstOperand().getResultType(), getSecondOperand().getResultType());
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // TODO Think about char type
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
            TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
            TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        } else {
            getFirstOperand().generateCode(currentClass, currentMethod, cv, mv);
            TypeTree.widen(mv, getFirstOperand().getResultType(), getResultType());

            getSecondOperand().generateCode(currentClass, currentMethod, cv, mv);
            TypeTree.widen(mv, getSecondOperand().getResultType(), getResultType());

            mv.visitInsn(Utility.getOpcode(getResultType().getTypeCode(), "ADD"));
        }
    }
}
