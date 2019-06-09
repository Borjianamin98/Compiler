package semantic.symbolTable;

import jdk.internal.org.objectweb.asm.Opcodes;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.Argument;

import java.util.List;
import java.util.Optional;

public class Utility {
    private Utility() {
    }

    public static String getTypePrefix(int type) {
        if (type == Constants.INTEGER_DSCP.getTypeCode() ||
                type == Constants.BOOLEAN_DSCP.getTypeCode() ||
                type == Constants.CHAR_DSCP.getTypeCode()) {
            return "I";
        } else if (type == Constants.LONG_DSCP.getTypeCode()) {
            return "L";
        } else if (type == Constants.DOUBLE_DSCP.getTypeCode()) {
            return "D";
        } else if (type == Constants.FLOAT_DSCP.getTypeCode()) {
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
        if (type == Constants.INTEGER_DSCP.getTypeCode()) {
            return "I";
        } else if (type == Constants.CHAR_DSCP.getTypeCode()) {
            return "C";
        } else if (type == Constants.LONG_DSCP.getTypeCode()) {
            return "J";
        } else if (type == Constants.DOUBLE_DSCP.getTypeCode()) {
            return "D";
        } else if (type == Constants.FLOAT_DSCP.getTypeCode()) {
            return "F";
        } else if (type == Constants.BOOLEAN_DSCP.getTypeCode()) {
            return "Z";
        } else if (type == Constants.VOID_DSCP.getTypeCode()) {
            return "V";
        } else if (type == Constants.STRING_DSCP.getTypeCode()) {
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

    public static String createArgumentDescriptor(List<Argument> arguments) {
        StringBuilder argumentDescriptor = new StringBuilder("(");
        if (arguments != null) {
            for (Argument argument : arguments)
                argumentDescriptor.append(argument.getDescriptor());
        }
        argumentDescriptor.append(")");
        return argumentDescriptor.toString();
    }

//    public static int checkType(int type1, int type2) {
//        if (type1 != type2)
//            throw new TypeMismatchException(type1 + " doesn't match with " + type2);
//        return type1;
////        if (type1.equals(Integer.class)) {
////            if (type2.equals(Double.class) || type2.equals(Float.class) ||
////                    type2.equals(Long.class) || type2.equals(Integer.class))
////                return type2;
////        } else if (type1.equals(Long.class)) {
////            if (type2.equals(Double.class) || type2.equals(Float.class) ||
////                    type2.equals(Long.class))
////                return type2;
////            else if (type2.equals(Integer.class))
////                return type1;
////        } else if (type1.equals(Float.class)) {
////            if (type2.equals(Double.class) || type2.equals(Float.class))
////                return type2;
////            else if (type2.equals(Integer.class) || type2.equals(Long.class))
////                return type1;
////        } else if (type1.equals(Double.class)) {
////            if (type2.equals(Double.class))
////                return type2;
////            else if (type2.equals(Integer.class) || type2.equals(Long.class) ||
////                    type2.equals(Float.class))
////                return type1;
////        } else if (type1.equals(String.class)) {
////            if (type2.equals(String.class))
////                return String.class;
////        }
////        throw new TypeMismatchException();
//    }
}
