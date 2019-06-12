package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

public class MemberVariable extends Variable {
    private Variable parent;
    private String memberName;
    private HasTypeDSCP dscp;
    private RecordTypeDSCP recordTypeDSCP;

    public MemberVariable(Variable parent, String memberName) {
        super(parent.getChainName() + "." + memberName);
        this.parent = parent;
        this.memberName = memberName;
    }

    public RecordTypeDSCP getRecordTypeDSCP() {
        return recordTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (!getDSCP().isInitialized())
            throw new RuntimeException(String.format("Variable %s might not have been initialized", getChainName()));
        parent.generateCode(currentClass, currentMethod, cv, mv);
        mv.visitFieldInsn(Opcodes.GETFIELD, recordTypeDSCP.getName(), memberName, dscp.getDescriptor());
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (getDSCP().isConstant() && getDSCP().isInitialized())
            throw new RuntimeException(String.format("Cannot assign a value to const variable %s. Variable %s already have been assigned",
                    getChainName(), getChainName()));
        parent.generateCode(currentClass, currentMethod, cv, mv);
        value.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, value.getResultType(), getResultType()); // right value must be converted to type of variable
        mv.visitFieldInsn(Opcodes.PUTFIELD, recordTypeDSCP.getName(), memberName, dscp.getDescriptor());
        getDSCP().setInitialized(true);
        // TODO Check initialization for array
        // setInitializationOfArray(getChainName(), value);
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            if (!(parent.getDSCP().getType() instanceof RecordTypeDSCP))
                throw new RuntimeException(parent.getChainName() + " is not a record");
            recordTypeDSCP = (RecordTypeDSCP) parent.getDSCP().getType();
            if (!(recordTypeDSCP.getField(memberName).isPresent()))
                throw new RuntimeException("Member field " + getChainName() + " doesn't exist");
            dscp = recordTypeDSCP.getField(memberName).get();
        }
        return dscp;
    }
}
