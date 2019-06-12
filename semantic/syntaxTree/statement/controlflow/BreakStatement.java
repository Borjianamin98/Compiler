package semantic.syntaxTree.statement.controlflow;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;

public class BreakStatement extends Statement {
    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (breakLabel != null)
            mv.visitJumpInsn(Opcodes.GOTO, breakLabel);
        else
            throw new RuntimeException("Break outside of loop or switch");
    }
}
