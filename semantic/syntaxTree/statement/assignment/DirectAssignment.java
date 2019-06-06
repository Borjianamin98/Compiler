package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.ConstantModificationException;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;

public class DirectAssignment extends Assignment {
    public DirectAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getValue().generateCode(cv, mv);
        if (getVariable().getDSCP().isConstant())
            throw new ConstantModificationException(getVariable().getName() + " can't not modified");
        // TODO Conversesion between types like int to double (i2d)
        mv.visitVarInsn(Utility.getOpcode(getVariable().getDSCP().getType(), "STORE"), getVariable().getDSCP().getAddress());
    }
}
