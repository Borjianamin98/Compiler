package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class ArrayVariable extends Variable {
    private Variable parent;
    private Expression requestedDimension;
    private HasTypeDSCP dscp;
    private ArrayTypeDSCP arrayTypeDSCP;

    /**
     * Descriptor of latest record which wrapped by this ArrayVariable
     */
    private RecordTypeDSCP parentRecordDSCP;

    public ArrayVariable(Variable parent, Expression requestedDimension) {
        super(parent.getChainName() + "[]");
        this.parent = parent;
        this.requestedDimension = requestedDimension;

    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (!getDSCP().isInitialized())
            throw new RuntimeException(String.format("Variable %s might not have been initialized", getChainName()));

        parent.generateCode(currentClass, currentMethod, cv, mv);

        requestedDimension.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, requestedDimension.getResultType(), TypeTree.INTEGER_DSCP);

        mv.visitInsn(Utility.getOpcode(arrayTypeDSCP.getInternalType(), "ALOAD", true));
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (getDSCP().isConstant() && getDSCP().isInitialized())
            throw new RuntimeException(String.format("Cannot assign a value to const variable %s. Variable %s already have been assigned",
                    getChainName(), getChainName()));

        parent.generateCode(currentClass, currentMethod, cv, mv);

        requestedDimension.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, requestedDimension.getResultType(), TypeTree.INTEGER_DSCP);

        value.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, value.getResultType(), getResultType()); // right value must be converted to type of variable

        mv.visitInsn(Utility.getOpcode(arrayTypeDSCP.getInternalType(), "ASTORE", true));
        getDSCP().setInitialized(true);
        // TODO Check initialization for array
        // setInitializationOfArray(getChainName(), value);
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            // handle index subscription for array type
            if (parent.getDSCP().getType() instanceof ArrayTypeDSCP) {
                arrayTypeDSCP = (ArrayTypeDSCP) parent.getDSCP().getType();

                if (parent instanceof MemberVariable)
                    parentRecordDSCP = ((MemberVariable) parent).getRecordTypeDSCP();
                else if (parent instanceof ArrayVariable)
                    parentRecordDSCP = ((ArrayVariable) parent).parentRecordDSCP;

                Optional<DSCP> fetchedDSCP;
                if (parentRecordDSCP == null)
                    fetchedDSCP = Display.find(parent.getDSCP().getName() + "[]");
                else
                    fetchedDSCP = parentRecordDSCP.find(parent.getDSCP().getName() + "[]");

                if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof HasTypeDSCP))
                    throw new RuntimeException(parent.getDSCP().getName() + "[]" + " is not declared");
                dscp = (HasTypeDSCP) fetchedDSCP.get();
            } else
                throw new RuntimeException("Call subscript is only for array: " + parent.getChainName());

        }
        return dscp;
    }
}
