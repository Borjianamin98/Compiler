package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.expression.operation.arithmetic.Divide;
import semantic.syntaxTree.expression.operation.arithmetic.Multiply;
import semantic.syntaxTree.program.ClassDCL;

public class DivideAssignment extends Assignment {
    public DivideAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Divide divide = new Divide(getVariable(), getValue());
        DirectAssignment assignment = new DirectAssignment(getVariable(), divide);
        assignment.generateCode(currentClass, currentMethod, cv, mv);
    }
}
