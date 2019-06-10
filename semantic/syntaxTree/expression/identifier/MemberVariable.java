package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.IllegalTypeException;
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
        this.parent = parent;
        this.memberName = memberName;
    }

    public RecordTypeDSCP getRecordTypeDSCP() {
        return recordTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        parent.generateCode(currentClass, currentMethod, cv, mv);
        if (!dscp.isInitialized())
            throw new RuntimeException("Field " + memberName + " of type " + dscp.getType().getName() + " is not initialized");
        mv.visitFieldInsn(Opcodes.GETFIELD, recordTypeDSCP.getName(), memberName, dscp.getDescriptor());
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        parent.generateCode(currentClass, currentMethod, cv, mv);
        value.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, getResultType(), value.getResultType()); // right value must be converted to type of variable
        mv.visitFieldInsn(Opcodes.PUTFIELD, recordTypeDSCP.getName(), memberName, dscp.getDescriptor());
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            if (!(parent.getDSCP().getType() instanceof RecordTypeDSCP))
                throw new IllegalTypeException(parent.getDSCP().getName() + " is not a record");
            recordTypeDSCP = (RecordTypeDSCP) parent.getDSCP().getType();
            if (!(recordTypeDSCP.getField(memberName).isPresent()))
                throw new IllegalTypeException("Member field " + memberName + " doesn't exist");
            dscp = recordTypeDSCP.getField(memberName).get();
        }
        return dscp;
    }
}
