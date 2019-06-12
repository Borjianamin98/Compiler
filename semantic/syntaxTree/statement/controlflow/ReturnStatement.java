package semantic.syntaxTree.statement.controlflow;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.typeTree.TypeTree;

public class ReturnStatement extends Statement {
    private Expression value;

    /**
     * return statement which return value expression
     *
     * @param value of return statement
     */
    public ReturnStatement(Expression value) {
        this.value = value;
    }

    /**
     * empty return statement
     */
    public ReturnStatement() {
        this(null);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (value != null) {
            if (!currentMethod.hasReturn())
                throw new RuntimeException("Unexpected return type: " + value.getResultType().getConventionalName());
            else {
                try {
                    value.generateCode(currentClass, currentMethod, cv, mv, null, null);
                    TypeTree.widen(mv, value.getResultType(), currentMethod.getReturnType()); // return value must be converted to return type of function
                } catch (RuntimeException ex) {
                    throw new RuntimeException("Unexpected return type: " + value.getResultType().getConventionalName());
                }
                mv.visitInsn(Utility.getOpcode(currentMethod.getReturnType(), "RETURN", false));
            }
        } else {
            if (currentMethod.hasReturn())
                throw new RuntimeException("Unexpected return type: " + currentMethod.getReturnType().getConventionalName());
            mv.visitInsn(Opcodes.RETURN);
        }
    }
}
