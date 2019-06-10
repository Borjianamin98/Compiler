package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.typeTree.TypeTree;

import java.util.Scanner;

public class ScannerFunction extends Expression implements BlockCode {
    private TypeDSCP requestedType;
    private boolean requestLine;

    /**
     * get a type from input stream
     * @param requestedType type which got from input
     */
    public ScannerFunction(TypeDSCP requestedType) {
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
    public TypeDSCP getResultType() {
        if (requestLine)
            return TypeTree.STRING_DSCP;
        if (!requestedType.isPrimitive() ||
                requestedType.getTypeCode() == TypeTree.VOID_DSCP.getTypeCode() ||
                requestedType.getTypeCode() == TypeTree.CHAR_DSCP.getTypeCode())
            throw new RuntimeException("Input a non-primitive value is not possible: " + requestedType.getConventionalName());
        return requestedType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // choose method name
        String scannerMethodName;
        if (requestLine)
            scannerMethodName = "nextLine";
        else if (requestedType.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode())
            scannerMethodName = "nextInt";
        else if (requestedType.getTypeCode() == TypeTree.BOOLEAN_DSCP.getTypeCode())
            scannerMethodName = "nextBoolean";
        else if (requestedType.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode())
            scannerMethodName = "nextLong";
        else if (requestedType.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode())
            scannerMethodName = "nextFloat";
        else if (requestedType.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode())
            scannerMethodName = "nextDouble";
        else if (requestedType.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode())
            scannerMethodName = "next";
        else
            throw new AssertionError("doesn't happen");

        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner",
                scannerMethodName,
                "()" + getResultType().getName(),
                false);
    }
}
