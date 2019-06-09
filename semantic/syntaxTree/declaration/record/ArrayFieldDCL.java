package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.Declaration;

import java.util.Optional;

public class ArrayFieldDCL extends Declaration {
    private String owner;
    private String descriptor;
    private int dimensions;
    private TypeDSCP baseType;
    private boolean initialized;

    public ArrayFieldDCL(String owner, String name, String type, String descriptor, int dimensions, boolean isConstant, boolean initialized) {
        super(name, type, isConstant);
        this.owner = owner;
        this.descriptor = descriptor;
        this.dimensions = dimensions;
        this.initialized = initialized;
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
            throw new DuplicateDeclarationException("Field " + getName() + " declared more than one time");
        if (dimensions <= 0)
            throw new RuntimeException("Filed array declaration must contain at least one dimension");

        getTypeDSCP();
        cv.visitField(Opcodes.ACC_PUBLIC, getName(), descriptor, null, null).visitEnd();
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
//        VariableDSCP variableDSCP = new VariableDSCP(lastDSCPName, lastDimensionType, 1, -1, isConstant(), initialized);
//        top.addSymbol(lastDSCPName, variableDSCP);
        FieldDSCP fieldDSCP = new FieldDSCP(owner, lastDSCPName, lastDimensionType, isConstant(), initialized);
        top.addSymbol(lastDSCPName, fieldDSCP);
    }
}
