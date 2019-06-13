package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.expression.operation.arithmetic.Reminder;
import semantic.syntaxTree.program.ClassDCL;

public class ReminderAssignment extends Assignment {
    public ReminderAssignment(Variable variable, Expression value) {
        super("-=", variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        Reminder reminder = new Reminder(getVariable(), getValue());
        DirectAssignment assignment = new DirectAssignment(getVariable(), reminder);
        assignment.generateCode(currentClass, currentMethod, cv, mv, null, null);
    }
}
