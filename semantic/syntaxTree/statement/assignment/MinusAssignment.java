package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.expression.operation.arithmetic.Minus;
import semantic.syntaxTree.expression.operation.arithmetic.Plus;
import semantic.syntaxTree.program.ClassDCL;

public class MinusAssignment extends Assignment {
    public MinusAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Minus minus = new Minus(getVariable(), getValue());
        DirectAssignment assignment = new DirectAssignment(getVariable(), minus);
        assignment.generateCode(currentClass, currentMethod, cv, mv);
    }
}
