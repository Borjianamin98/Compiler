package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.ConstantModificationException;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;

public class DirectAssignment extends Assignment {
    public DirectAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (getVariable().getDSCP().isConstant())
            throw new ConstantModificationException("Variable can't not modified");
        // TODO Conversesion between types like int to double (i2d)
        getVariable().assignValue(cv, mv, getValue());
        getVariable().getDSCP().setInitialized(true);
    }
}
