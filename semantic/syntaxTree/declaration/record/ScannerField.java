package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.typeTree.TypeTree;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

public class ScannerField extends Field {
    public ScannerField() {
        super(TypeTree.SCANNER_FIELD_NAME, null, 0, null, true, true);
    }

    public Declaration createFieldDCL(String owner) {
        return new SimpleFieldDCL(owner, name, baseType, true, true, true) {
            @Override
            public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
                int access = Opcodes.ACC_PUBLIC;
                access |= isConstant() ? Opcodes.ACC_FINAL : 0;
                access |= isStatic() ? Opcodes.ACC_STATIC : 0;
                cv.visitField(access, getName(), TypeTree.SCANNER_JAVA_TYPE, null, null).visitEnd();
            }
        };
    }

    @Override
    public boolean hasDefaultValue() {
        return true;
    }

    @Override
    public String getDescriptor() {
        return TypeTree.SCANNER_JAVA_TYPE;
    }

    public void generateCode(String owner, ClassVisitor cv, MethodVisitor mv) {
        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, getName(), getDescriptor());
    }

    @Override
    public String getCodeRepresentation() {
        return "const scanner " + getName();
    }
}
