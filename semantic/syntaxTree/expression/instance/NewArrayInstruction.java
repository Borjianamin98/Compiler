package semantic.syntaxTree.expression.instance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;

import java.util.List;

public class NewArrayInstruction extends Expression {
    private String type;
    private TypeDSCP typeDSCP;
    private List<Expression> dimensions;
    private ArrayTypeDSCP resultType;

    public NewArrayInstruction(String type, List<Expression> dimensions) {
        this.type = type;
        this.dimensions = dimensions;
    }

    public int getDimensionsCount() {
        return dimensions.size();
    }

    /**
     * @return TypeDSCP of base type which is created by new array instruction
     */
    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null)
            typeDSCP = Display.getType(type);
        return typeDSCP;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimensions.size());
    }

    @Override
    public ArrayTypeDSCP getResultType() {
        if (resultType == null)
            resultType = Utility.addArrayType(getTypeDSCP(), dimensions.size());
        return resultType;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (dimensions == null || dimensions.size() == 0)
            throw new RuntimeException("Array declaration must contains at least one dimension");

        getResultType();
        for (Expression dim : dimensions) {
            dim.generateCode(currentClass, currentMethod, cv, mv, null, null);
            if (dim.getResultType().getTypeCode() != TypeTree.INTEGER_DSCP.getTypeCode())
                throw new RuntimeException("Dimension of array must be integer type");
        }
        mv.visitMultiANewArrayInsn(getDescriptor(), dimensions.size());
    }

    @Override
    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        represent.append("new ").append(Utility.getConvectionalRepresent(type));
        for (Expression dimension : dimensions) {
            represent.append("[").append(dimension.getCodeRepresentation()).append("]");
        }
        return represent.toString();
    }
}
