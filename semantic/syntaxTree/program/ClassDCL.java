package semantic.syntaxTree.program;

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
import semantic.syntaxTree.declaration.record.ArrayFieldDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.declaration.record.RecordTypeDCL;
import semantic.syntaxTree.declaration.record.SimpleFieldDCL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ClassDCL extends Node {
    private String name;
    private List<Field> fields;
    private List<MethodDCL> methods;
    private List<RecordTypeDCL> records;

    public ClassDCL(String name, List<Field> fields, List<MethodDCL> methods, List<RecordTypeDCL> records) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
        this.records = records;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name, null, "java/lang/Object", null);

        Display.add(false); // Class symbol table
        if (fields != null) {
            for (Field field : fields) {
                Declaration fieldDCL;
                if (field.isArray()) {
                    fieldDCL = new ArrayFieldDCL(name, field.getName(), field.getBaseType(), field.getDimensions(),
                            field.isConstant(), field.getDefaultValue() != null, false);
                } else {
                    fieldDCL = new SimpleFieldDCL(name, field.getName(), field.getBaseType(), field.isConstant(),
                            field.getDefaultValue() != null, false);
                }
                fieldDCL.generateCode(classWriter, null);
            }
        }

        // Default constructor of class
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        if (fields != null) {
            for (Field field : fields) {
                if (field.getDefaultValue() != null) {
                    if (field.isStatic()) {
                        field.getDefaultValue().generateCode(classWriter, methodVisitor);
                        methodVisitor.visitFieldInsn(Opcodes.PUTSTATIC, name, field.getName(), field.getDescriptor());
                    } else {
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
                        field.getDefaultValue().generateCode(classWriter, methodVisitor);
                        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, name, field.getName(), field.getDescriptor());
                    }
                }
            }
        }

        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        classWriter.visitEnd();

        // Generate Methods
        if (methods != null) {
            for (MethodDCL method : methods) {
                method.generateCode(classWriter, mv);
            }
        }

        // Generate Records
        if (records != null) {
            for (RecordTypeDCL record : records) {
                record.generateCode(classWriter, mv);
            }
        }

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
    }
}
