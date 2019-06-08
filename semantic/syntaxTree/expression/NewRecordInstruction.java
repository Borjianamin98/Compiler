package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Optional;

public class NewRecordInstruction extends Expression {
    private String type;
    private RecordTypeDSCP typeDSCP;

    public NewRecordInstruction(String type) {
        this.type = type;
    }

    private RecordTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(type);
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof RecordTypeDSCP))
                throw new SymbolNotFoundException("Type " + type + " not found");
            typeDSCP = (RecordTypeDSCP) fetchedDSCP.get();
        }
        return typeDSCP;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), 0);
    }

    @Override
    public ArrayTypeDSCP getResultType() {
        if (super.getResultType() == null)
            setResultType(getTypeDSCP());
        return (ArrayTypeDSCP) super.getResultType();
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getTypeDSCP();
        mv.visitTypeInsn(Opcodes.NEW, type);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, type, "<init>", "()V", false);
    }
}
