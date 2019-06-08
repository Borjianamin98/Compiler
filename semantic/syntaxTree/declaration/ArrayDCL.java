package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.identifier.Variable;

import java.util.List;

public class ArrayDCL extends Declaration {
    private List<Integer> dimension;

    public ArrayDCL(String name, String type, boolean isConstant, List<Integer> dimension) {
        super(name, type, isConstant);
        this.dimension = dimension;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimension.size());
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (dimension.isEmpty())
            throw new RuntimeException("Array declaration must contain a array with at least one dimension");

        // id$x means x-th dimension of id
        // id$0 means id itself too

        SymbolTable top = Display.top();
        // TODO What to do about initialization of array
        TypeDSCP lastDimensionType = getTypeDSCP();
        VariableDSCP variableDSCP = null;
        for (int i = dimension.size() - 1; i >= 0; i--) {
            TypeDSCP typeDSCP = new ArrayTypeDSCP(lastDimensionType);
            top.addType(typeDSCP.getName(), typeDSCP);
            DSCP descriptor = null;
            if (i == 0) {
                // TODO Think about initialization value
                descriptor = variableDSCP = new VariableDSCP(getName() + "$" + i, typeDSCP, 1, top.getFreeAddress(), isConstant(), true);
            } else {
                descriptor = new ArrayDSCP(getName() + "$" + i, typeDSCP, lastDimensionType, dimension.get(i), false);
            }
            top.addSymbol(descriptor.getName(), descriptor);
            lastDimensionType = typeDSCP;
        }

        for (Integer dim : dimension)
            mv.visitLdcInsn(dim);
        mv.visitMultiANewArrayInsn(getDescriptor(), dimension.size());
        mv.visitVarInsn(Opcodes.ASTORE, variableDSCP.getAddress());
    }
}
