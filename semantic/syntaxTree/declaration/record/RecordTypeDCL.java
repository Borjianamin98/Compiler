package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RecordTypeDCL extends Node {
    private String name;
    private List<Field> fields;

    public RecordTypeDCL(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name, null, "java/lang/Object", null);

        // Record symbol table
        Display.add(false);
        for (Field field : fields) {
            field.setStatic(false);
            Declaration fieldDCL;
            if (field.isArray()) {
                fieldDCL = new ArrayFieldDCL(name, field.getName(), field.getBaseType(), field.getDimensions(),
                        field.isConstant(), field.getDefaultValue() != null, field.isStatic());
            } else {
                fieldDCL = new SimpleFieldDCL(name, field.getName(), field.getBaseType(), field.isConstant(),
                        field.getDefaultValue() != null, field.isStatic());
            }
            fieldDCL.generateCode(null, null, classWriter, null);
        }

        // Constructor of record
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        for (Field field : fields) {
            if (field.getDefaultValue() != null) {
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
                field.getDefaultValue().generateCode(null, null, classWriter, methodVisitor);
                methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, name, field.getName(), field.getDescriptor());
            }
        }


        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        classWriter.visitEnd();

        // Generate class file
        try (FileOutputStream fos = new FileOutputStream(Node.outputPath + name + ".class")) {
            fos.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update symbol table
        if (Display.find(name).isPresent()) {
            throw new DuplicateDeclarationException("Identifier " + name + " declared more than one time");
        }
        RecordTypeDSCP recordDSCP = new RecordTypeDSCP(name, 1, Display.pop());
        SymbolTable.addType(name, recordDSCP);
    }
}
