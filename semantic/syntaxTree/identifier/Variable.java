package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

public abstract class Variable extends Expression {
    public abstract void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract HasTypeDSCP getDSCP();

}
