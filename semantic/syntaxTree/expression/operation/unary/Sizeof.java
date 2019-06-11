package semantic.syntaxTree.expression.operation.unary;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.identifier.SimpleType;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class Sizeof extends Expression {
    private SimpleType type;

    public Sizeof(SimpleType type) {
        this.type = type;
    }

    @Override
    public SimpleTypeDSCP getResultType() {
        type.getTypeDSCP();
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        int size;
        if (type.getTypeDSCP().isPrimitive()) {
            if (type.getTypeDSCP().getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode()) {
                size = Integer.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.BOOLEAN_DSCP.getTypeCode()) {
                size = Short.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.CHAR_DSCP.getTypeCode()) {
                size = Character.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
                size = Long.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
                size = Double.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
                size = Float.BYTES;
            } else if (type.getTypeDSCP().getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
                size = Integer.BYTES; // a pointer/reference
            } else
                size = 0; // void type
        } else
            size = Integer.BYTES; // a pointer/reference
        new IntegerConst(size).generateCode(currentClass, currentMethod, cv, mv);
    }
}
