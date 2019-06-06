package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.VariableDCL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RecordDCL extends Node {
    private String name;
    private List<VariableDCL> variableDCLS;

    public RecordDCL(String name, List<VariableDCL> variableDCLS) {
        this.name = name;
        this.variableDCLS = variableDCLS;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name, null, "java/lang/Object", null);
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();


        // Fieldssssssssssssssssss
        for (VariableDCL variableDCL : variableDCLS) {
            variableDCL.generateCode(classWriter, methodVisitor);
        }
        classWriter.visitEnd();

        try (FileOutputStream fos = new FileOutputStream(Node.outputPath + name + ".class")) {
            fos.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
