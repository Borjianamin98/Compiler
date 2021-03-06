package semantic.syntaxTree.expression.operation.unary.prefix_postfix;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.expression.operation.arithmetic.Minus;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.assignment.DirectAssignment;

public class MinusMinusPrefix extends OneIncrementAndDecrement {
    public MinusMinusPrefix(Variable variable) {
        super("prefix '--'", variable);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        TypeDSCP resultType = getResultType();
        Expression oneConst = Utility.getSimpleConstant(resultType, 1);
        new DirectAssignment(getVariable(), new Minus(getVariable(), oneConst)).generateCode(currentClass, currentMethod, cv, mv, null, null);
        getVariable().generateCode(currentClass, currentMethod, cv, mv, null, null);
    }

    @Override
    public String getCodeRepresentation() {
        return "--( " + getVariable().getCodeRepresentation() + " )";
    }
}
