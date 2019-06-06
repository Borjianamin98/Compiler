package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.ConstantModificationException;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;

public class MultiplyAssignment extends Assignment {
    public MultiplyAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getVariable().generateCode(cv, mv);
        getValue().generateCode(cv, mv);
        if (getVariable().getDSCP().isConstant())
            throw new ConstantModificationException(getVariable().getName() + " can't not modified");
        mv.visitInsn(Utility.getOpcode(getVariable().getDSCP().getType().getTypeCode(), "MUL"));
        mv.visitVarInsn(Utility.getOpcode(getVariable().getDSCP().getType().getTypeCode(), "STORE"), getVariable().getDSCP().getAddress());
    }
}
