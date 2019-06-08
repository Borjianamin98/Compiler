package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Optional;

public class ArrayDCL extends Declaration {
    private int dimensions;
    private TypeDSCP baseType;

    public ArrayDCL(String name, String type, boolean isConstant, int dimensions) {
        super(name, type, isConstant);
        this.dimensions = dimensions;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimensions);
    }

    @Override
    public ArrayTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getType());
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException("Type " + getType() + " not found");
            baseType = (TypeDSCP) fetchedDSCP.get();
            typeDSCP = Utility.addArrayType(baseType, dimensions);
        }
        return (ArrayTypeDSCP) typeDSCP;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        SymbolTable top = Display.top();
        if (top.contain(getName()))
            throw new DuplicateDeclarationException(getName() + " declared more than one time");
        if (dimensions == 0)
            throw new RuntimeException("Array declaration must contain at least one dimension");

        TypeDSCP lastDimensionType = getTypeDSCP();
        String lastDSCPName = getName();
        for (int i = 0; i <= dimensions - 1; i++) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) lastDimensionType;
            DSCP descriptor = new ArrayDSCP(lastDSCPName, arrayTypeDSCP, arrayTypeDSCP.getInternalType(), baseType,
                    i == 0 ? top.getFreeAddress() : -1, false, true);
            top.addSymbol(descriptor.getName(), descriptor);
            lastDimensionType = arrayTypeDSCP.getInternalType();
            lastDSCPName = lastDSCPName + "[]";
        }
        VariableDSCP variableDSCP = new VariableDSCP(lastDSCPName, lastDimensionType, 1, -1, isConstant(), false);
        top.addSymbol(lastDSCPName, variableDSCP);
    }
}
