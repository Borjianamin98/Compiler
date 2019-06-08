package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.IllegalTypeException;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.syntaxTree.expression.Expression;

public class MemberVariable extends Variable {
    private Variable parent;
    private String memberName;
    private HasTypeDSCP parentDSCP;
    private RecordTypeDSCP recordTypeDSCP;

    public MemberVariable(Variable parent, String memberName) {
        this.parent = parent;
        this.memberName = memberName;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        // TODO Check initialization
//        if (!parentDSCP.isInitialized())
//            throw new RuntimeException("Variable " + getName() + " is not initialized");
        parent.generateCode(cv, mv);
        mv.visitFieldInsn(Opcodes.GETFIELD, recordTypeDSCP.getName(), memberName, recordTypeDSCP.getField(memberName).getDescriptor());
        setResultType(parentDSCP.getType());
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        parent.generateCode(cv, mv);
        value.generateCode(cv, mv);
        mv.visitFieldInsn(Opcodes.PUTFIELD, recordTypeDSCP.getName(), memberName, recordTypeDSCP.getField(memberName).getDescriptor());
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (parentDSCP == null) {
            if (!(parent.getDSCP().getType() instanceof RecordTypeDSCP))
                throw new IllegalTypeException(parent.getDSCP().getName() + " is not a record");
            recordTypeDSCP = (RecordTypeDSCP) parent.getDSCP().getType();
            if (!(recordTypeDSCP.containsField(memberName)))
                throw new IllegalTypeException("Member field " + memberName + " doesn't exist");
            parentDSCP = recordTypeDSCP.getField(memberName);
        }
        return parentDSCP;
    }
}
