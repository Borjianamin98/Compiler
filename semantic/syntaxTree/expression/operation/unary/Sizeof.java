package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class Sizeof extends Expression {
    private String typeName;

    public Sizeof(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public SimpleTypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        TypeDSCP typeDSCP = Display.getType(typeName);
        int size;
        if (typeDSCP.isPrimitive()) {
            if (typeDSCP.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode()) {
                size = Integer.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.BOOLEAN_DSCP.getTypeCode()) {
                size = Short.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.CHAR_DSCP.getTypeCode()) {
                size = Character.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
                size = Long.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
                size = Double.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
                size = Float.BYTES;
            } else if (typeDSCP.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
                size = Integer.BYTES; // a pointer/reference
            } else
                size = 0; // void typeName
        } else
            size = Integer.BYTES; // a pointer/reference
        new IntegerConst(size).generateCode(currentClass, currentMethod, cv, mv, null, null);
    }

    @Override
    public String getCodeRepresentation() {
        return "sizeof(" + Utility.getConvetionalRepresent(typeName) + ")";
    }
}
