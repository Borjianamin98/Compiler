package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.arithmetic.Plus;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;

public class PlusAssignment extends Assignment {
    public PlusAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Plus plus = new Plus(getVariable(), getValue());
        DirectAssignment assignment = new DirectAssignment(getVariable(), plus);
        assignment.generateCode(currentClass, currentMethod, cv, mv);
    }
}
