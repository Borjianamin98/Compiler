package semantic.syntaxTree.expression.instance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Optional;

public class NewRecordInstruction extends Expression {
    private String type;
    private RecordTypeDSCP typeDSCP;

    public NewRecordInstruction(String type) {
        this.type = type;
    }

    private RecordTypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            TypeDSCP fetchedDSCP = Display.getType(type);
            if (!(fetchedDSCP instanceof RecordTypeDSCP))
                throw new RuntimeException("Type " + type + " is not declared");
            typeDSCP = (RecordTypeDSCP) fetchedDSCP;
        }
        return typeDSCP;
    }

    private String getDescriptor() {
        return Utility.getDescriptor(getTypeDSCP(), 0);
    }

    @Override
    public RecordTypeDSCP getResultType() {
        return getTypeDSCP();
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getTypeDSCP();
        mv.visitTypeInsn(Opcodes.NEW, type);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, type, "<init>", "()V", false);
    }
}
