package semantic.syntaxTree.expression.instance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.List;
import java.util.Optional;

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
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(type);
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new RuntimeException("Type " + type + " not found");
            typeDSCP = (TypeDSCP) fetchedDSCP.get();
        }
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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        if (dimensions == null || dimensions.size() == 0)
            throw new RuntimeException("Array declaration must contain at least one dimension");

        for (Expression dim : dimensions) {
            dim.generateCode(currentClass, currentMethod, cv, mv);
            if (dim.getResultType().getTypeCode() != TypeTree.INTEGER_DSCP.getTypeCode())
                throw new RuntimeException("Dimension of array must be integer type");
        }
        mv.visitMultiANewArrayInsn(getDescriptor(), dimensions.size());
    }
}
