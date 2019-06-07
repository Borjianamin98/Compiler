package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.TypeDSCP;

import java.util.List;

public class ArrayDCL extends Declaration {
    private List<Integer> dimension;

    public ArrayDCL(String name, String type, boolean isConstant, List<Integer> dimension) {
        super(name, type, isConstant, null);
        this.dimension = dimension;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimension.size());
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (dimension.isEmpty())
            throw new RuntimeException("Array declaration must contain a array with at least one dimension");
        // TODO What to do about initialization of array
        for (Integer integer : dimension) {
            mv.visitLdcInsn(integer);
        }
        mv.visitMultiANewArrayInsn(getDescriptor(), dimension.size());
    }
}
