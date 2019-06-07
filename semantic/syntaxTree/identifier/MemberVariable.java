package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.variable.SimpleVariableDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class MemberVariable extends Variable {
    private String parent;
    private SimpleVariableDSCP parentDSCP;
    private RecordTypeDSCP recordTypeDSCP;

    public MemberVariable(String parent, String name) {
        super(name);
        this.parent = parent;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        mv.visitVarInsn(Opcodes.ALOAD, parentDSCP.getAddress());
        mv.visitFieldInsn(Opcodes.GETFIELD, parentDSCP.getType().getName(), getName(), recordTypeDSCP.getField(getName()).getDescriptor());
        setResultType(recordTypeDSCP.getField(getName()).getType());
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        mv.visitVarInsn(Opcodes.ALOAD, parentDSCP.getAddress());
        value.generateCode(cv, mv);
        mv.visitFieldInsn(Opcodes.PUTFIELD, parentDSCP.getType().getName(), getName(), recordTypeDSCP.getField(getName()).getDescriptor());
    }

    @Override
    public SimpleVariableDSCP getDSCP() {
        if (parentDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(parent);
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(parent + " is not declared");
            if (fetchedDSCP.get() instanceof SimpleVariableDSCP) {
                parentDSCP = (SimpleVariableDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(parent + " is not a variable");
            if (!(parentDSCP.getType() instanceof RecordTypeDSCP) ||
                    !(((RecordTypeDSCP) parentDSCP.getType()).containsField(getName())))
                throw new IllegalTypeException(parent + " doesn't have member " + getName());
            recordTypeDSCP = (RecordTypeDSCP) parentDSCP.getType();
        }
        return parentDSCP;
    }
}
