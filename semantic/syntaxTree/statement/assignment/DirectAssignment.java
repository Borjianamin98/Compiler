package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;

public class DirectAssignment extends Assignment {
    public DirectAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getVariable().assignValue(currentClass, currentMethod, cv, mv, getValue());
    }
}
