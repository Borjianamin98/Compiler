package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Constants;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.List;

public class ArrayDCL extends Declaration {
    /**
     * only one of this available at any time
     */
    private int arrayLevel;
    private Expression defaultValue;
    private List<Expression> dimension;
    /**
     * if it is true, then make a NEW instruction for array
     * you can not initialize it again with ArrayDCL (get duplicate symbol exception)
     */
    private boolean heapInit;

    public ArrayDCL(String name, String type, boolean isConstant, List<Expression> dimension) {
        super(name, type, isConstant);
        this.dimension = dimension;
        this.arrayLevel = dimension.size();
        this.heapInit = true;
    }

    public ArrayDCL(String name, String type, boolean isConstant, int arrayLevel, Expression defaultValue) {
        super(name, type, isConstant);
        this.arrayLevel = arrayLevel;
        this.defaultValue = defaultValue;
        this.heapInit = false;
    }

    public boolean isHeapInit() {
        return heapInit;
    }

    public void setHeapInit(boolean heapInit) {
        this.heapInit = heapInit;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), dimension.size());
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (arrayLevel == 0)
            throw new RuntimeException("Array declaration must contain a array with at least one dimension");

        // id$x means x-th dimension of id
        // id$0 means id itself too

        SymbolTable top = Display.top();
        // TODO What to do about initialization of array
        TypeDSCP lastDimensionType = getTypeDSCP();
        VariableDSCP variableDSCP = null;
        for (int i = arrayLevel - 1; i >= 0; i--) {
            TypeDSCP typeDSCP;
            if ((typeDSCP = SymbolTable.getType("[" + lastDimensionType.getDescriptor())) == null) {
                typeDSCP = new ArrayTypeDSCP(arrayLevel - i, lastDimensionType, getTypeDSCP());
                SymbolTable.addType(typeDSCP.getName(), typeDSCP);
            }
            DSCP descriptor;
            if (i == 0) {
                // TODO Think about initialization value and virtual creation
                descriptor = variableDSCP = new VariableDSCP(getName() + "$" + i, typeDSCP, 1, top.getFreeAddress(), isConstant(), true);
            } else {
                descriptor = new ArrayDSCP(getName() + "$" + i, typeDSCP, lastDimensionType, getTypeDSCP(), arrayLevel - i, false);
            }
            top.addSymbol(descriptor.getName(), descriptor);
            lastDimensionType = typeDSCP;
        }

        if (heapInit) {
            for (Expression dim : dimension) {
                dim.generateCode(cv, mv);
                if (dim.getResultType().getTypeCode() != Constants.INTEGER_DSCP.getTypeCode())
                    throw new RuntimeException("Dimension of array must be int");
            }
            mv.visitMultiANewArrayInsn(getDescriptor(), dimension.size());
            mv.visitVarInsn(Opcodes.ASTORE, variableDSCP.getAddress());
        } else {
            if (defaultValue != null) {
                defaultValue.generateCode(cv, mv);
                mv.visitVarInsn(Utility.getOpcode(defaultValue.getResultType().getTypeCode(), "STORE"),
                        variableDSCP.getAddress());
                variableDSCP.setInitialized(true);
            }
        }
    }
}
