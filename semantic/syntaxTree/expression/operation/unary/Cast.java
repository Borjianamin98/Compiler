package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class Cast extends Expression {
    private String castType;
    private TypeDSCP castTypeDSCP;
    private Expression operand;

    public Cast(String castType, Expression operand) {
        this.castType = castType;
        this.operand = operand;
    }

    @Override
    public TypeDSCP getResultType() {
        if (castTypeDSCP == null)
            castTypeDSCP = Display.getType(castType);
        if (!castTypeDSCP.isPrimitive() || TypeTree.isVoid(castTypeDSCP) || TypeTree.isString(castTypeDSCP))
            throw new RuntimeException(castType + " is not a primitive type");
        return castTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        operand.generateCode(currentClass, currentMethod, cv, mv, null, null);
        try {
            // maybe a implicit (widen) cast is enough
            TypeTree.widen(mv, operand.getResultType(), getResultType());
        } catch (RuntimeException ex) {
            // otherwise do explicit (narrow) cast
            TypeTree.narrow(mv, operand.getResultType(), getResultType());
        }
    }

    @Override
    public String getCodeRepresentation() {
        return "(" + getResultType().getConventionalName() + ")(" + operand.getCodeRepresentation() + ")";
    }
}
