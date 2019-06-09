package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.ConstantModificationException;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class DirectAssignment extends Assignment {
    public DirectAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        if (getVariable().getDSCP().isConstant())
            throw new ConstantModificationException("Variable can't not modified");
        getVariable().assignValue(currentClass, currentMethod, cv, mv, getValue());
        getVariable().getDSCP().setInitialized(true);
    }
}
