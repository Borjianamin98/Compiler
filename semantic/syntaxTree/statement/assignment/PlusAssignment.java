package semantic.syntaxTree.statement.assignment;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;

public class PlusAssignment extends Assignment {
    public PlusAssignment(Variable variable, Expression value) {
        super(variable, value);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
//        getVariable().generateCode(cv, mv);
//        getValue().generateCode(cv, mv);
//        mv.visitInsn(Utility.getOpcode(getVariable().getDSCP().getBaseType().getTypeCode(), "ADD"));
//        mv.visitVarInsn(Utility.getOpcode(getVariable().getDSCP().getBaseType().getTypeCode(), "STORE"), getVariable().getDSCP().getAddress());
//        getVariable().getDSCP().setInitialized(true);
    }
}
