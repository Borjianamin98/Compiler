package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.expression.Expression;

public abstract class Variable extends Expression {
    public abstract void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract HasTypeDSCP getDSCP();

}
