package semantic.syntaxTree.expression.call;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.Ignorable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class ScannerFunction extends Expression implements BlockCode, Ignorable {
    private String requestedType;
    private TypeDSCP requestedTypeDSCP;
    private boolean requestLine;

    /**
     * if result of method must be discarded, you must set this to true
     * so result of function (if exists) will be pop from operand stack
     * this will set with parser, but if you test it by your own, set it
     * to true
     */
    private boolean ignoreResult;

    /**
     * get a type from input stream
     * @param requestedType type which got from input
     */
    public ScannerFunction(String requestedType) {
        this.requestedType = requestedType;
        this.requestLine = false;
    }

    /**
     * return next line of input
     */
    public ScannerFunction() {
        this.requestLine = true;
    }

    @Override
    public void setIgnoreResult(boolean ignoreResult) {
        this.ignoreResult = ignoreResult;
    }

    @Override
    public TypeDSCP getResultType() {
        if (requestLine)
            return TypeTree.STRING_DSCP;
        if (requestedTypeDSCP == null)
            requestedTypeDSCP = Display.getType(requestedType);
        if (!requestedTypeDSCP.isPrimitive() ||
                requestedTypeDSCP.getTypeCode() == TypeTree.VOID_DSCP.getTypeCode() ||
                requestedTypeDSCP.getTypeCode() == TypeTree.CHAR_DSCP.getTypeCode())
            throw new RuntimeException("Input a non-primitive value is not possible: " + requestedTypeDSCP.getConventionalName());
        return requestedTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getResultType();
        // choose method name
        String scannerMethodName;
        if (requestLine)
            scannerMethodName = "nextLine";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode())
            scannerMethodName = "nextInt";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.BOOLEAN_DSCP.getTypeCode())
            scannerMethodName = "nextBoolean";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode())
            scannerMethodName = "nextLong";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode())
            scannerMethodName = "nextFloat";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode())
            scannerMethodName = "nextDouble";
        else if (requestedTypeDSCP.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode())
            scannerMethodName = "next";
        else
            throw new AssertionError("doesn't happen");

        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner",
                scannerMethodName,
                "()" + Utility.getPrimitiveTypeName(getResultType()),
                false);
        if (ignoreResult) {
            mv.visitInsn(Opcodes.POP);
        }
    }
}
