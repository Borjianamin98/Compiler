package semantic.typeTree;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.HashMap;
import java.util.Map;

public class TypeTree {
    private static Map<TypeDSCP, TypeNode> typeMapping = new HashMap<>();
    public static final SimpleTypeDSCP INTEGER_DSCP = new SimpleTypeDSCP("int", semantic.typeTree.TypeTree.INTEGER_SIZE);
    public static final SimpleTypeDSCP BOOLEAN_DSCP = new SimpleTypeDSCP("bool", semantic.typeTree.TypeTree.INTEGER_SIZE);
    public static final SimpleTypeDSCP LONG_DSCP = new SimpleTypeDSCP("long", semantic.typeTree.TypeTree.LONG_SIZE);
    public static final SimpleTypeDSCP FLOAT_DSCP = new SimpleTypeDSCP("float", semantic.typeTree.TypeTree.FLOAT_SIZE);
    public static final SimpleTypeDSCP DOUBLE_DSCP = new SimpleTypeDSCP("double", semantic.typeTree.TypeTree.DOUBLE_SIZE);
    public static final SimpleTypeDSCP CHAR_DSCP = new SimpleTypeDSCP("char", semantic.typeTree.TypeTree.CHAR_SIZE);
    public static final SimpleTypeDSCP STRING_DSCP = new SimpleTypeDSCP("string", semantic.typeTree.TypeTree.STRING_SIZE);
    public static final SimpleTypeDSCP VOID_DSCP = new SimpleTypeDSCP("void", 0);

    private static final int INTEGER_SIZE = 1;
    private static final int LONG_SIZE = 2;
    private static final int FLOAT_SIZE = 1;
    private static final int DOUBLE_SIZE = 2;
    private static final int CHAR_SIZE = 1;
    private static final int STRING_SIZE = -1;

    static {
        typeMapping.put(DOUBLE_DSCP, new TypeNode(null, DOUBLE_DSCP, 0));
        typeMapping.put(FLOAT_DSCP, new TypeNode(typeMapping.get(DOUBLE_DSCP), FLOAT_DSCP));
        typeMapping.put(LONG_DSCP, new TypeNode(typeMapping.get(FLOAT_DSCP), LONG_DSCP));
        typeMapping.put(INTEGER_DSCP, new TypeNode(typeMapping.get(LONG_DSCP), INTEGER_DSCP));
        typeMapping.put(CHAR_DSCP, new TypeNode(typeMapping.get(INTEGER_DSCP), CHAR_DSCP));
        typeMapping.put(BOOLEAN_DSCP, new TypeNode(typeMapping.get(INTEGER_DSCP), BOOLEAN_DSCP));
    }

    /**
     * @param type1 type 1
     * @param type2 type 2
     * @return top type which type1 and type2 can be converted (widened) to it, otherwise throw exception
     * @throws RuntimeException
     */
    public static TypeDSCP max(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return type1;
        TypeNode typeNode1 = typeMapping.get(type1);
        TypeNode typeNode2 = typeMapping.get(type2);
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

    private static void throwIncompatibleTypeException(TypeDSCP type1, TypeDSCP type2) {
        throw new RuntimeException("Incompatible types: " + type2.getName() + " cannot be converted to " + type1.getName());
    }

    public static boolean isString(TypeDSCP type) {
        return type.getTypeCode() == STRING_DSCP.getTypeCode();
    }

    /**
     * check type is integer or long
     * @param type type
     * @return true if type is integer or long
     */
    public static boolean isInteger(TypeDSCP type) {
        return type.getTypeCode() == INTEGER_DSCP.getTypeCode() || type.getTypeCode() == LONG_DSCP.getTypeCode();
    }

    /**
     * check whether type1 can be converted (widened) to type2
     * @param type1 type1
     * @param type2 type2
     * @return true if convert (widen) can happen
     */
    public static boolean canWiden(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return true;
        TypeNode typeNode1 = typeMapping.get(type1);
        TypeNode typeNode2 = typeMapping.get(type2);
        if (typeNode1 == null || typeNode2 == null)
            return false;
        while (typeNode1.getLevel() != 0 && typeNode1.getType().getTypeCode() != type2.getTypeCode()) {
            typeNode1 = typeNode1.getParent();
        }
        return typeNode1.getType().getTypeCode() == type2.getTypeCode();
    }

    /**
     * return level difference of two type in tree (0 if two type are equal)
     * @param type1 type1
     * @param type2 type2
     * @return level(type2) - level(type1)
     * @throws RuntimeException if type1 can not converted (widened) to type2
     */
    public static int diffLevel(TypeDSCP type1, TypeDSCP type2) {
        if (type1.getTypeCode() == type2.getTypeCode())
            return 0;
        TypeNode typeNode1 = typeMapping.get(type1);
        TypeNode typeNode2 = typeMapping.get(type2);
        TypeNode typeNode1Copy = typeMapping.get(type1);
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
