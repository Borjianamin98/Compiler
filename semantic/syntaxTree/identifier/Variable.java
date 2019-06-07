package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.VariableDSCP;
import semantic.syntaxTree.expression.Expression;

public abstract class Variable extends Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public abstract void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract VariableDSCP getDSCP();

    public String getName() {
        return name;
    }
}
