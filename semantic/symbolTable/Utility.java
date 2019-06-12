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
import semantic.syntaxTree.expression.constValue.DoubleConst;
import semantic.syntaxTree.expression.constValue.FloatConst;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.constValue.LongConst;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Utility {
    private Utility() {
    }

    /**
     * return prefix appropriate for a opcode of a type
     * @param type type
     * @param realReturn if true, return 'c' instead of 'i' for character and same for other types
     *                   purpose of this is because of reference to char array. fo some opcode like
     *                   castore, caload, ... we must use prefix 'c' instead of 'i'
     * @return appropriate prefix
     */
    private static String getTypePrefix(TypeDSCP type, boolean realReturn) {
        if (type.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode() ||
                type.getTypeCode() == TypeTree.BOOLEAN_DSCP.getTypeCode()) {
            return "I";
        } else if (type.getTypeCode() == TypeTree.CHAR_DSCP.getTypeCode()) {
            return realReturn ? "C" : "I";
        } else if (type.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            return "L";
        } else if (type.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            return "D";
        } else if (type.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            return "F";
        } else
            return "A";
    }

    /**
     * return opcode created by prefix + instruction + postfix
     * @param prefix prefix of opcode
     * @param instruction main instruction
     * @param postfix postfix of opcode
     * @return appropriate opcode
     * @throws RuntimeException if opcode not found
     */
    public static int getOpcode(String prefix, String instruction, String postfix) {
        try {
            return (int) Opcodes.class.getDeclaredField(prefix + instruction + postfix).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Not found requested opcode");
        }
    }

    /**
     * return opcode created by prefix + instruction based on type
     * @param type type of opcode
     * @param instruction main instruction
     * @param realReturn if true, return 'c' instead of 'i' for character and same for other types
     * @return appropriate opcode
     * @throws RuntimeException if opcode not found
     */
    public static int getOpcode(TypeDSCP type, String instruction, boolean realReturn) {
        try {
            return (int) Opcodes.class.getDeclaredField(getTypePrefix(type, realReturn) + instruction).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Not found requested opcode");
        }
    }


    /**
     * get a base type and add all type of [[..(type) to current symbol table
     * if they are not present
     * @param baseType   base type of array
     * @param dimensions count of dimensions for type creating
     * @return last dimension which is created: ([[.. : length of it is dimension)(type)
     * @throws IllegalArgumentException if dimension is less than or equal zero
     */
    public static ArrayTypeDSCP addArrayType(TypeDSCP baseType, int dimensions) {
        if (dimensions <= 0)
            throw new IllegalArgumentException("Dimensions must be greater than zero");
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

    /**
     * get primitive type name without prefix which is added to them (TypeTree.typePrefix)
     * @param type type
     * @return descriptor of primitive type
     * @throws RuntimeException if type is not primitive
     */
    public static String getPrimitiveTypeName(TypeDSCP type) {
        if (!type.isPrimitive())
            throw new RuntimeException(type.getName() + " is not primitive");
        return type.getName().replace(TypeTree.typePrefix, "");
    }

    /**
     * create descriptor of a type with requested dimensions
     * @param type       type
     * @param dimensions dimensions
     * @return descriptor of requested type
     */
    public static String getDescriptor(TypeDSCP type, int dimensions) {
        StringBuilder desc = new StringBuilder(String.join("", Collections.nCopies(dimensions, "[")));
        if (type.isPrimitive())
            desc.append(getPrimitiveTypeName(type));
        else
            desc.append('L').append(type.getName()).append(";");

        return desc.toString();
    }

    public static String createMethodDescriptor(List<Argument> arguments, boolean hasReturn, TypeDSCP returnType) {
        StringBuilder methodDescriptor = new StringBuilder(createArgumentDescriptor(arguments));
        if (hasReturn) {
            if (returnType.isPrimitive())
                methodDescriptor.append(getPrimitiveTypeName(returnType));
            else
                methodDescriptor.append("L").append(returnType.getName()).append(";");
        } else
            methodDescriptor.append("V");
        return methodDescriptor.toString();
    }

    /**
     * create a string which represent method call with passed arguments to it
     *
     * @param functionName name of function
     * @param parameters   parameters passed for method call
     * @return string which represent method call: function(parm1, parm2, ...)
     */
    public static String createMethodCallDescriptor(String functionName, List<Expression> parameters) {
        StringBuilder methodCallDescriptor = new StringBuilder(functionName).append("(");
        if (parameters != null) {
            for (Expression parameter : parameters)
                methodCallDescriptor.append(parameter.getResultType().getConventionalName());
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
     *
     * @param currentClass   current class which booleanExpr is a part of it
     * @param currentMethod  current method which booleanExpr is a part of it
     * @param cv             current class visitor
     * @param mv             current method visitor
     * @param booleanExpr    expression which is evaluated as boolean
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
     *
     * @param currentClass  current class which booleanExpr is a part of it
     * @param currentMethod current method which booleanExpr is a part of it
     * @param cv            current class visitor
     * @param mv            current method visitor
     * @param booleanExpr   expression which is evaluated as boolean
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

    /**
     * create a expression which is a constant with requested value
     *
     * @param type  type of expression
     * @param value value of expression
     * @return expression of type with constant value
     * @throws IllegalArgumentException if type is not a primitive type (except void and string type)
     */
    public static Expression getSimpleConstant(TypeDSCP type, int value) {
        if (type.getTypeCode() == TypeTree.INTEGER_DSCP.getTypeCode())
            return new IntegerConst(value);
        else if (type.getTypeCode() == TypeTree.LONG_DSCP.getTypeCode()) {
            return new LongConst(value);
        } else if (type.getTypeCode() == TypeTree.FLOAT_DSCP.getTypeCode()) {
            return new FloatConst(value);
        } else if (type.getTypeCode() == TypeTree.DOUBLE_DSCP.getTypeCode()) {
            return new DoubleConst(value);
        } else
            throw new IllegalArgumentException("Type must be a primitive type: " + type.getConventionalName());
    }

    /**
     * check if a type is reference or not. a reference type can contain null value
     * @param type type
     * @return true if type is a reference, otherwise false
     */
    public static boolean isReferenceType(TypeDSCP type) {
        return !type.isPrimitive() ||
                TypeTree.isString(type) ||
                TypeTree.isVoid(type);
    }

}
