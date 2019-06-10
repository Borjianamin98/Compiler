package semantic.syntaxTree.statement.controlflow;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;

public class BreakStatement extends Statement {
    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        throw new RuntimeException("Break statement represent a definition");
    }
}
