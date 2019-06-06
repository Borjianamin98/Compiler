package semantic.symbolTable;

import jdk.internal.org.objectweb.asm.Opcodes;
import semantic.Constants;
import semantic.syntaxTree.declaration.method.Argument;

import java.util.List;

public class Utility {
    private Utility() {
    }

    public static String getTypePrefix(int type) {
        switch (type) {
            case Constants.INTEGER_CODE:
            case Constants.BOOLEAN_CODE:
            case Constants.CHAR_CODE:
                return "I";
            case Constants.LONG_CODE:
                return "L";
            case Constants.DOUBLE_CODE:
                return "D";
            case Constants.FLOAT_CODE:
                return "F";
            default:
                return "A";
        }
    }

    public static int getOpcode(String prefix, String instruction) {
        try {
            return (int) Opcodes.class.getDeclaredField(prefix + instruction).get(null);
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
        switch (type) {
            case Constants.INTEGER_CODE:
                return "I";
            case Constants.CHAR_CODE:
                return "C";
            case Constants.LONG_CODE:
                return "J";
            case Constants.DOUBLE_CODE:
                return "D";
            case Constants.FLOAT_CODE:
                return "F";
            case Constants.BOOLEAN_CODE:
                return "Z";
            case Constants.VOID_CODE:
                return "V";
            case Constants.STRING_CODE:
                return "Ljava/lang/String;";
            default:
                throw new RuntimeException(type + " is not a primitive type");
        }
    }

    public static String createMethodDescriptor(List<Argument> arguments, boolean hasReturn, int returnType) {
        StringBuilder methodDescriptor = new StringBuilder(createArgumentDescriptor(arguments));
        if (hasReturn)
            methodDescriptor.append(Utility.getPrimitiveTypeDescriptor(returnType));
        else
            methodDescriptor.append("V");
        return methodDescriptor.toString();
    }

    public static String createArgumentDescriptor(List<Argument> arguments) {
        StringBuilder argumentDescriptor = new StringBuilder("(");
        if (arguments != null) {
            for (Argument argument : arguments) {
                argumentDescriptor.append(argument.getDescriptor());
            }
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
