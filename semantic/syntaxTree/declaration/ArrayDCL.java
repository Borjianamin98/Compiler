package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Optional;

public class ArrayDCL extends Declaration {
    private String type;
    private TypeDSCP typeDSCP;
    private int dimensions;
    private TypeDSCP baseType;
    private boolean initialized;

    public ArrayDCL(String name, String type, int dimensions, boolean isConstant, boolean initialized) {
        super(name, isConstant);
        this.type = type;
        this.dimensions = dimensions;
        this.initialized = initialized;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimensions);
    }

    public ArrayTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(type);
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new RuntimeException("Type " + type + " not found");
            baseType = (TypeDSCP) fetchedDSCP.get();
            typeDSCP = Utility.addArrayType(baseType, dimensions);
        }
        return (ArrayTypeDSCP) typeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        SymbolTable top = Display.top();
        if (top.contain(getName()))
            throw new RuntimeException(getName() + " declared more than one time");
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
        VariableDSCP variableDSCP = new VariableDSCP(lastDSCPName, lastDimensionType, 1, -1, isConstant(), initialized);
        top.addSymbol(lastDSCPName, variableDSCP);
    }
}
