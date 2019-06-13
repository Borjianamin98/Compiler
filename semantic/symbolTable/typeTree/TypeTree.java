package semantic.symbolTable.typeTree;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.HashMap;
import java.util.Map;

public class TypeTree {
    /**
     * a map from each type to its node in widening tree
     * if a type is not present in tree, it means that type can only converted to same type (nothing else)
     */
    private static Map<TypeDSCP, TypeNode> wideningTree = new HashMap<>();
    public static final SimpleTypeDSCP INTEGER_DSCP = new SimpleTypeDSCP(TypeTree.INTEGER_NAME, TypeTree.INTEGER_SIZE);
    public static final SimpleTypeDSCP BOOLEAN_DSCP = new SimpleTypeDSCP(TypeTree.BOOLEAN_NAME, TypeTree.INTEGER_SIZE);
    public static final SimpleTypeDSCP LONG_DSCP = new SimpleTypeDSCP(TypeTree.LONG_NAME, TypeTree.LONG_SIZE);
    public static final SimpleTypeDSCP FLOAT_DSCP = new SimpleTypeDSCP(TypeTree.FLOAT_NAME, TypeTree.FLOAT_SIZE);
    public static final SimpleTypeDSCP DOUBLE_DSCP = new SimpleTypeDSCP(TypeTree.DOUBLE_NAME, TypeTree.DOUBLE_SIZE);
    public static final SimpleTypeDSCP CHAR_DSCP = new SimpleTypeDSCP(TypeTree.CHAR_NAME, TypeTree.CHAR_SIZE);
    public static final SimpleTypeDSCP STRING_DSCP = new SimpleTypeDSCP(TypeTree.STRING_NAME, TypeTree.STRING_SIZE);
    public static final SimpleTypeDSCP VOID_DSCP = new SimpleTypeDSCP(TypeTree.VOID_NAME, 0);

    /**
     * prefix type$ is ued because these are predefined types however
     * a programmer can define a type/variable with name of I, Z, ...
     */
    public static final String typePrefix = "type$";
    public static final String INTEGER_NAME = typePrefix + "I";
    public static final String BOOLEAN_NAME = typePrefix + "Z";
    public static final String LONG_NAME = typePrefix + "J";
    public static final String FLOAT_NAME = typePrefix + "F";
    public static final String DOUBLE_NAME = typePrefix + "D";
    public static final String CHAR_NAME = typePrefix + "C";
    public static final String STRING_NAME = typePrefix + "Ljava/lang/String;";
    public static final String VOID_NAME = typePrefix + "V";
    public static final String AUTO_NAME = typePrefix + "AUTO";

    private static final int INTEGER_SIZE = 1;
    private static final int LONG_SIZE = 2;
    private static final int FLOAT_SIZE = 1;
    private static final int DOUBLE_SIZE = 2;
    private static final int CHAR_SIZE = 1;
    private static final int STRING_SIZE = 1;

    public static final String SCANNER_FIELD_NAME = "field$scanner";
    public static final String SCANNER_JAVA_TYPE = "Ljava/util/Scanner;";

    public static void init() {
        wideningTree.put(DOUBLE_DSCP, new TypeNode(null, DOUBLE_DSCP, 0));
        wideningTree.put(FLOAT_DSCP, new TypeNode(wideningTree.get(DOUBLE_DSCP), FLOAT_DSCP));
        wideningTree.put(LONG_DSCP, new TypeNode(wideningTree.get(FLOAT_DSCP), LONG_DSCP));
        wideningTree.put(INTEGER_DSCP, new TypeNode(wideningTree.get(LONG_DSCP), INTEGER_DSCP));
        wideningTree.put(CHAR_DSCP, new TypeNode(wideningTree.get(INTEGER_DSCP), CHAR_DSCP));
        wideningTree.put(BOOLEAN_DSCP, new TypeNode(wideningTree.get(INTEGER_DSCP), BOOLEAN_DSCP));
    }

    /**
     * @param type1 type 1
     * @param type2 type 2
     * @return top type which type1 and type2 can be converted (widened) to it, otherwise throw exception
     * @throws RuntimeException if types are not convertible to each other
     *                          (this is recognized if one of type are not in widen tree)
     */
    public static TypeDSCP max(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return type1;
        TypeNode typeNode1 = wideningTree.get(type1);
        TypeNode typeNode2 = wideningTree.get(type2);
        if (typeNode1 == null || typeNode2 == null)
            throwIncompatibleTypeException(type1, type2);
        while (typeNode1.getLevel() > typeNode2.getLevel())
            typeNode1 = typeNode1.getParent();
        while (typeNode2.getLevel() > typeNode1.getLevel())
            typeNode2 = typeNode2.getParent();
        while (typeNode1.getType().getTypeCode() != typeNode2.getType().getTypeCode()) {
            typeNode1 = typeNode1.getParent();
            typeNode2 = typeNode2.getParent();
        }
        return typeNode1.getType();
    }

    /**
     * generate byte code to convert (widen) type1 to type2
     *
     * @param type1 real type of operand on top of operand stack
     * @param type2 type to which operand stack will converted (widened)
     * @throws RuntimeException if type1 can not widen to type2
     */
    public static void widen(MethodVisitor mv, TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return;
        if (type1.getTypeCode() == INTEGER_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == LONG_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.I2L);
            else if (type2.getTypeCode() == FLOAT_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.I2F);
            else if (type2.getTypeCode() == DOUBLE_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.I2D);
            else
                throwIncompatibleTypeException(type1, type2);
        } else if (type1.getTypeCode() == LONG_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == FLOAT_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.L2F);
            else if (type2.getTypeCode() == DOUBLE_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.L2D);
            else
                throwIncompatibleTypeException(type1, type2);
        } else if (type1.getTypeCode() == FLOAT_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == DOUBLE_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.F2D);
            else
                throwIncompatibleTypeException(type1, type2);
        } else
            throwIncompatibleTypeException(type1, type2);
    }

    /**
     * generate byte code to convert (narrow) type1 to type2
     *
     * @param type1 real type of operand on top of operand stack
     * @param type2 type to which operand stack will converted (narrowed)
     * @throws RuntimeException if type1 can not narrow to type2
     */
    public static void narrow(MethodVisitor mv, TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return;
        if (!type1.isPrimitive() || !type2.isPrimitive())
            throw new RuntimeException("Narrow cast is only for primitive types: " + type1.getConventionalName() + " and " + type2.getConventionalName());
        if (type1.getTypeCode() == DOUBLE_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == FLOAT_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.D2F);
            else if (type2.getTypeCode() == LONG_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.D2L);
            else if (type2.getTypeCode() == INTEGER_DSCP.getTypeCode() ||
                    type2.getTypeCode() == CHAR_DSCP.getTypeCode() ||
                    type2.getTypeCode() == BOOLEAN_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.D2I);
            else
                throwIncompatibleTypeException(type1, type2);
        } else if (type1.getTypeCode() == FLOAT_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == LONG_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.F2L);
            else if (type2.getTypeCode() == INTEGER_DSCP.getTypeCode() ||
                    type2.getTypeCode() == CHAR_DSCP.getTypeCode() ||
                    type2.getTypeCode() == BOOLEAN_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.F2I);
            else
                throwIncompatibleTypeException(type1, type2);
        } else if (type1.getTypeCode() == LONG_DSCP.getTypeCode()) {
            if (type2.getTypeCode() == INTEGER_DSCP.getTypeCode() ||
                    type2.getTypeCode() == CHAR_DSCP.getTypeCode() ||
                    type2.getTypeCode() == BOOLEAN_DSCP.getTypeCode())
                mv.visitInsn(Opcodes.F2I);
            else
                throwIncompatibleTypeException(type1, type2);
        } else
            throwIncompatibleTypeException(type1, type2);
    }

    private static void throwIncompatibleTypeException(TypeDSCP type1, TypeDSCP type2) {
        throw new RuntimeException("Incompatible types: " + type1.getConventionalName() + " cannot be converted to " + type2.getConventionalName());
    }

    public static boolean isString(TypeDSCP type) {
        return type.getTypeCode() == STRING_DSCP.getTypeCode();
    }

    public static boolean isVoid(TypeDSCP type) {
        return type.getTypeCode() == VOID_DSCP.getTypeCode();
    }

    /**
     * check type is integer or long
     *
     * @param type type
     * @return true if type is integer or long
     */
    public static boolean isInteger(TypeDSCP type) {
        return type.getTypeCode() == INTEGER_DSCP.getTypeCode() || type.getTypeCode() == LONG_DSCP.getTypeCode();
    }

    /**
     * check whether type1 can be converted (widened) to type2
     *
     * @param type1 type1
     * @param type2 type2
     * @return true if convert (widen) can happen
     */
    public static boolean canWiden(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return true;
        TypeNode typeNode1 = wideningTree.get(type1);
        TypeNode typeNode2 = wideningTree.get(type2);
        if (typeNode1 == null || typeNode2 == null)
            return false;
        while (typeNode1.getLevel() != 0 && typeNode1.getType().getTypeCode() != type2.getTypeCode()) {
            typeNode1 = typeNode1.getParent();
        }
        return typeNode1.getType().getTypeCode() == type2.getTypeCode();
    }

    /**
     * return level difference of two type in tree (0 if two type are equal)
     *
     * @param type1 type1
     * @param type2 type2
     * @return level(type2) - level(type1)
     * @throws RuntimeException if type1 can not converted (widened) to type2
     */
    public static int diffLevel(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return 0;
        TypeNode typeNode1 = wideningTree.get(type1);
        TypeNode typeNode2 = wideningTree.get(type2);
        TypeNode typeNode1Copy = wideningTree.get(type1);
        if (typeNode1 == null || typeNode2 == null)
            throwIncompatibleTypeException(type1, type2);
        while (typeNode1.getLevel() != 0 && typeNode1.getType().getTypeCode() != type2.getTypeCode()) {
            typeNode1 = typeNode1.getParent();
        }
        if (typeNode1.getType().getTypeCode() == type2.getTypeCode())
            return typeNode1Copy.getLevel() - typeNode2.getLevel();
        else
            throwIncompatibleTypeException(type1, type2);
        throw new AssertionError("doesn't happen");
    }
}
