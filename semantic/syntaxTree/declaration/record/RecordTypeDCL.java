package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.*;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordTypeDCL extends Declaration implements BlockCode, ClassCode {
    private List<Field> fields;

    public RecordTypeDCL(String name, List<Field> fields) {
        super(name, false);
        this.fields = fields;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, getName(), null, "java/lang/Object", null);

        // Record symbol table
        Display.add(false);
        List<Field> fields_need_initialized = new ArrayList<>();
        for (Field field : fields) {
            field.setStatic(false);
            field.createFieldDCL(currentClass.getName()).generateCode(currentClass, null, classWriter, null, null, null);
            if (field.getDefaultValue() != null)
                fields_need_initialized.add(field);
        }

        // Constructor of record
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        // initialize field
        // do initialization of field which are non-static in default constructor
        for (Field field : fields_need_initialized) {
            if (!field.isStatic())
                field.generateCode(getName(), classWriter, methodVisitor);
        }

        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        classWriter.visitEnd();

        // Generate class file
        try (FileOutputStream fos = new FileOutputStream(Node.outputPath + getName() + ".class")) {
            fos.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update symbol table
        if (Display.find(getName()).isPresent()) {
            throw new RuntimeException(getName() + " declared more than one time");
        }
        RecordTypeDSCP recordDSCP = new RecordTypeDSCP(getName(), 1, Display.pop());
        Display.addType(getName(), recordDSCP);
    }
}
