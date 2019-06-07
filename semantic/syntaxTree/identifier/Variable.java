package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.variable.SimpleVariableDSCP;
import semantic.syntaxTree.expression.Expression;

public abstract class Variable extends Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public abstract void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract SimpleVariableDSCP getDSCP();

    public String getName() {
        return name;
    }
}
