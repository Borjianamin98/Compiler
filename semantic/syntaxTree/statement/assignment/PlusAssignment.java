package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;

public class PlusAssignment extends Assignment {
    public PlusAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getVariable().generateCode(cv, mv);
        getValue().generateCode(cv, mv);
        mv.visitInsn(Utility.getOpcode(getVariable().getDSCP().getType().getTypeCode(), "ADD"));
        mv.visitVarInsn(Utility.getOpcode(getVariable().getDSCP().getType().getTypeCode(), "STORE"), getVariable().getDSCP().getAddress());
        getVariable().getDSCP().setInitialized(true);
    }
}
