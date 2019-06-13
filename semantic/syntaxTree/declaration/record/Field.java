package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.HasRepresentation;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.Parameter;
import semantic.syntaxTree.expression.Expression;
import semantic.symbolTable.typeTree.TypeTree;

public class Field extends Parameter implements ClassCode, HasRepresentation {
    private Expression defaultValue;
    private boolean constant;
    private boolean isStatic;

    /**
     * create a field for class/record based on available information
     *
     * @param name         name of field
     * @param baseType     base type of field
     * @param dimensions   dimensions of field
     * @param defaultValue default value of field (if null, meaning no initialization)
     * @param constant     true if field is constant (final)
     * @param isStatic     true if field is static
     */
    public Field(String name, String baseType, int dimensions, Expression defaultValue, boolean constant, boolean isStatic) {
        super(name, baseType, dimensions);
        this.constant = constant;
        this.defaultValue = defaultValue;
        this.isStatic = isStatic;
    }

    /**
     * create a field for class/record based on available information
     * type of field detected automatically from initialization (Auto field declaration)
     *
     * @param name         name of field
     * @param defaultValue default value of field
     * @param constant     true if field is constant (final)
     * @param isStatic     true if field is static
     */
    public Field(String name, Expression defaultValue, boolean constant, boolean isStatic) {
        super(name, defaultValue.getResultType().getName(), 0);
        this.constant = constant;
        this.defaultValue = defaultValue;
        this.isStatic = isStatic;
        if (defaultValue.getResultType() instanceof SimpleTypeDSCP ||
                defaultValue.getResultType() instanceof RecordTypeDSCP) {
            // nothing to do
        } else if (defaultValue.getResultType() instanceof ArrayTypeDSCP) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) defaultValue.getResultType();
            baseType = arrayTypeDSCP.getBaseType().getName();
            dimensions = arrayTypeDSCP.getDimensions();
        } else {
            throw new RuntimeException("Can not detect type for auto variable");
        }
    }

    /**
     * generate a field declaration depend on information of field
     * (ArrayFieldDCL or SimpleFieldDCL)
     *
     * @param owner owner of field (name of class)
     */
    public Declaration createFieldDCL(String owner) {
        // Create field
        if (isArray())
            return new ArrayFieldDCL(owner, name, baseType, dimensions, constant, defaultValue != null, isStatic);
        else
            return new SimpleFieldDCL(owner, name, baseType, constant, defaultValue != null, isStatic);
    }

    /**
     * initialize a field with it's default value
     * it must be called inside default constructor (if it is non-static)
     * or inside static constructor (if it is static)
     * @param owner owner of field
     * @param cv class visitor
     * @param mv method visitor
     */
    public void generateCode(String owner, ClassVisitor cv, MethodVisitor mv) {
        if (defaultValue != null) {
            if (isStatic) {
                defaultValue.generateCode(null, null, cv, mv, null, null);
                TypeTree.widen(mv, defaultValue.getResultType(), getType()); // right value must be converted to type of field
                mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, getName(), getDescriptor());
            } else {
                mv.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
                defaultValue.generateCode(null, null, cv, mv, null, null);
                TypeTree.widen(mv, defaultValue.getResultType(), getType()); // right value must be converted to type of field
                mv.visitFieldInsn(Opcodes.PUTFIELD, owner, getName(), getDescriptor());
            }
        }
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public boolean isConstant() {
        return constant;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    @Override
    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        if (isConstant())
            represent.append("const ");
        represent.append(Utility.getConvectionalRepresent(baseType));
        for (int i = 0; i < getDimensions(); i++) {
            represent.append("[]");
        }
        represent.append(" ").append(getName());
        return represent.toString();
    }
}
