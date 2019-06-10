package semantic.symbolTable;

import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.Argument;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.List;

public class Utility {
    private Utility() {
    }

    public static String getTypePrefix(int type) {
        if (type == TypeTree.INTEGER_DSCP.getTypeCode() ||
                type == TypeTree.BOOLEAN_DSCP.getTypeCode() ||
                type == TypeTree.CHAR_DSCP.getTypeCode()) {
            return "I";
        } else if (type == TypeTree.LONG_DSCP.getTypeCode()) {
            return "L";
        } else if (type == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            return "D";
        } else if (type == TypeTree.FLOAT_DSCP.getTypeCode()) {
            return "F";
        }
        return "A";
    }

    public static int getOpcode(String prefix, String instruction, String postfix) {
        try {
            return (int) Opcodes.class.getDeclaredField(prefix + instruction + postfix).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Not found requested opcode");
        }
    }

    public static int getOpcode(int type, String instruction) {
        try {
            return (int) Opcodes.class.getDeclaredField(getTypePrefix(type) + instruction).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Not found requested opcode");
        }
    }

    public static String getPrimitiveTypeDescriptor(int type) {
        if (type == TypeTree.INTEGER_DSCP.getTypeCode()) {
            return "I";
        } else if (type == TypeTree.CHAR_DSCP.getTypeCode()) {
            return "C";
        } else if (type == TypeTree.LONG_DSCP.getTypeCode()) {
            return "J";
        } else if (type == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            return "D";
        } else if (type == TypeTree.FLOAT_DSCP.getTypeCode()) {
            return "F";
        } else if (type == TypeTree.BOOLEAN_DSCP.getTypeCode()) {
            return "Z";
        } else if (type == TypeTree.VOID_DSCP.getTypeCode()) {
            return "V";
        } else if (type == TypeTree.STRING_DSCP.getTypeCode()) {
            return "Ljava/lang/String;";
        }
        throw new RuntimeException(type + " is not a primitive type");
    }

    public static ArrayTypeDSCP addArrayType(TypeDSCP baseType, int dimensions) {
        if (dimensions <= 0)
            throw new RuntimeException("Dimensions must be greater than zero");
        TypeDSCP lastDimensionType = baseType;
        for (int i = dimensions - 1; i >= 0; i--) {
            TypeDSCP typeDSCP;
            if ((typeDSCP = SymbolTable.getType("[" + lastDimensionType.getDescriptor())) == null) {
                typeDSCP = new ArrayTypeDSCP(lastDimensionType, baseType);
                SymbolTable.addType(typeDSCP.getName(), typeDSCP);
            }
            lastDimensionType = typeDSCP;
        }
        return (ArrayTypeDSCP) lastDimensionType;
    }

    public static String getDescriptor(TypeDSCP type, int arrayLevel) {
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < arrayLevel; i++) {
            desc.append('[');
        }
        if (type.isPrimitive()) {
            desc.append(Utility.getPrimitiveTypeDescriptor(type.getTypeCode()));
        } else {
            desc.append('L').append(type.getName()).append(";");
        }
        return desc.toString();
    }

    public static String createMethodDescriptor(List<Argument> arguments, boolean hasReturn, TypeDSCP returnType) {
        StringBuilder methodDescriptor = new StringBuilder(createArgumentDescriptor(arguments));
        if (hasReturn) {
            if (returnType.isPrimitive())
                methodDescriptor.append(Utility.getPrimitiveTypeDescriptor(returnType.getTypeCode()));
            else
                methodDescriptor.append("L").append(returnType.getName()).append(";");
        } else
            methodDescriptor.append("V");
        return methodDescriptor.toString();
    }

    /**
     * create a string which represent method call with passed arguments to it
     * @param functionName name of function
     * @param parameters parameters passed for method call
     * @return string which represent method call: function(parm1, parm2, ...)
     */
    public static String createMethodCallDescriptor(String functionName, List<Expression> parameters) {
        StringBuilder methodCallDescriptor = new StringBuilder(functionName).append("(");
        if (parameters != null) {
            for (Expression parameter : parameters)
                methodCallDescriptor.append(parameter.getResultType().getName());
        }
        methodCallDescriptor.append(")");
        return methodCallDescriptor.toString();
    }

    public static String createArgumentDescriptor(List<Argument> arguments) {
        StringBuilder argumentDescriptor = new StringBuilder("(");
        if (arguments != null) {
            for (Argument argument : arguments)
                argumentDescriptor.append(argument.getDescriptor());
        }
        argumentDescriptor.append(")");
        return argumentDescriptor.toString();
    }

    /**
     * generate a code which evaluate booleanExpr and a jump code to jump to falseJumpLabel
     * if boolean expression result is false
     * @param currentClass current class which booleanExpr is a part of it
     * @param currentMethod current method which booleanExpr is a part of it
     * @param cv current class visitor
     * @param mv current method visitor
     * @param booleanExpr expression which is evaluated as boolean
     * @param falseJumpLabel jump label for (booleanExpr == false)
     * @throws RuntimeException if expression is not a boolean expression
     */
    public static void evaluateBooleanExpressionFalse(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                                                      Expression booleanExpr, Label falseJumpLabel) {
        TypeDSCP booleanExprType = booleanExpr.getResultType();
        booleanExpr.generateCode(currentClass, currentMethod, cv, mv);
        if (booleanExprType.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode()) {
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, falseJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.LCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.LCMP);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, falseJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.FCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.FCMPL);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, falseJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.DCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.DCMPL);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, falseJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
            mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, falseJumpLabel);
        } else
            throw new RuntimeException("Invalid boolean expression: " + booleanExpr.getResultType().getConventionalName());
    }

    /**
     * generate a code which evaluate booleanExpr and a jump code to jump to falseJumpLabel
     * if boolean expression result is true
     * @param currentClass current class which booleanExpr is a part of it
     * @param currentMethod current method which booleanExpr is a part of it
     * @param cv current class visitor
     * @param mv current method visitor
     * @param booleanExpr expression which is evaluated as boolean
     * @param trueJumpLabel jump label for (booleanExpr == true)
     * @throws RuntimeException if expression is not a boolean expression
     */
    public static void evaluateBooleanExpressionTrue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                                                      Expression booleanExpr, Label trueJumpLabel) {
        TypeDSCP booleanExprType = booleanExpr.getResultType();
        booleanExpr.generateCode(currentClass, currentMethod, cv, mv);
        if (booleanExprType.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode()) {
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, trueJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.LCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.LCMP);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, trueJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.FCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.FCMPL);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, trueJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            mv.visitInsn(org.objectweb.asm.Opcodes.DCONST_0);
            mv.visitInsn(org.objectweb.asm.Opcodes.DCMPL);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFNE, trueJumpLabel);
        } else if (booleanExprType.getTypeCode() == TypeTree.STRING_DSCP.getTypeCode()) {
            mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false);
            mv.visitJumpInsn(org.objectweb.asm.Opcodes.IFEQ, trueJumpLabel);
        } else
            throw new RuntimeException("Invalid boolean expression: " + booleanExpr.getResultType().getName());
    }
}
