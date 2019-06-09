package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Constants;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.List;
import java.util.Optional;

public class NewArrayInstruction extends Expression {
    private String type;
    private TypeDSCP typeDSCP;
    private List<Expression> dimensions;

    public NewArrayInstruction(String type, List<Expression> dimensions) {
        this.type = type;
        this.dimensions = dimensions;
    }

    private TypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(type);
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException("Type " + type + " not found");
            typeDSCP = (TypeDSCP) fetchedDSCP.get();
        }
        return typeDSCP;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimensions.size());
    }

    @Override
    public ArrayTypeDSCP getResultType() {
        if (super.getResultType() == null)
            setResultType(Utility.addArrayType(getTypeDSCP(), dimensions.size()));
        return (ArrayTypeDSCP) super.getResultType();
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (dimensions == null || dimensions.size() == 0)
            throw new RuntimeException("Array declaration must contain at least one dimension");

        for (Expression dim : dimensions) {
            dim.generateCode(cv, mv);
            if (dim.getResultType().getTypeCode() != Constants.INTEGER_DSCP.getTypeCode())
                throw new RuntimeException("Dimension of array must be integer type");
        }
        mv.visitMultiANewArrayInsn(getDescriptor(), dimensions.size());
    }
}
