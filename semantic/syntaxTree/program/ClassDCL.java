package semantic.syntaxTree.program;

import org.objectweb.asm.*;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.record.ArrayFieldDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.declaration.record.RecordTypeDCL;
import semantic.syntaxTree.declaration.record.SimpleFieldDCL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ClassDCL extends Node {
    private String name;
    private List<ClassCode> classCodes;

    public ClassDCL(String name) {
        this.name = name;
        this.classCodes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addClassCode(ClassCode classCode) {
        classCodes.add(classCode);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name, null, "java/lang/Object", null);

        // Default constructor of class
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        Display.add(false); // Class symbol table

        List<Field> fields_need_initialized = new ArrayList<>();
        for (ClassCode classCode : classCodes) {
            if (classCode instanceof Field) {
                createFieldDCLCode(classWriter, (Field) classCode);
                if (((Field) classCode).getDefaultValue() != null)
                    fields_need_initialized.add((Field) classCode);
            } else if (classCode instanceof MethodDCL || classCode instanceof RecordTypeDCL)
                ((Declaration) classCode).generateCode(this, null, classWriter, null, null, null);
        }

        // Check all method declaration are provided (not prototype)
        for (ClassCode classCode : classCodes) {
            if (classCode instanceof MethodDCL) {
                MethodDCL methodDCL = (MethodDCL) classCode;
                Optional<DSCP> dscp = Display.find(methodDCL.getName());
                if (!dscp.isPresent() || !(dscp.get() instanceof MethodDSCP))
                    throw new AssertionError("doesn't happen");
                ((MethodDSCP) dscp.get()).checkAllSignaturesDeclared();
            }
        }

        // initialize field
        // do initialization of field which are non-static in default constructor
        Iterator<Field> iterator = fields_need_initialized.iterator();
        while (iterator.hasNext()) {
            Field nextField = iterator.next();
            if (!nextField.isStatic()) {
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
                nextField.getDefaultValue().generateCode(this, null, classWriter, methodVisitor, null, null);
                methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, name, nextField.getName(), nextField.getDescriptor());

                // remove non-static fields
                iterator.remove();
            }
        }

        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        classWriter.visitEnd();

        // initialize field
        // do initialization of field which are static in static constructor
        methodVisitor = classWriter.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
        methodVisitor.visitCode();
        for (Field field : fields_need_initialized) {
            field.getDefaultValue().generateCode(this, null, classWriter, methodVisitor, null, null);
            methodVisitor.visitFieldInsn(Opcodes.PUTSTATIC, name, field.getName(), field.getDescriptor());
        }
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();


        // Generate class file
        try (FileOutputStream fos = new FileOutputStream(Node.outputPath + name + ".class")) {
            fos.write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update symbol table
        if (Display.find(name).isPresent()) {
            throw new RuntimeException("Identifier " + name + " declared more than one time");
        }
    }

    /**
     * generate field of class depend of filed type and it's dimension
     * it's initialization will do in another method
     *
     * @param classWriter class writer
     * @param field       filed
     */
    private void createFieldDCLCode(ClassWriter classWriter, Field field) {
        // Create field
        Declaration fieldDCL;
        if (field.isArray()) {
            fieldDCL = new ArrayFieldDCL(name, field.getName(), field.getBaseType(), field.getDimensions(),
                    field.isConstant(), field.getDefaultValue() != null, field.isStatic());
        } else {
            fieldDCL = new SimpleFieldDCL(name, field.getName(), field.getBaseType(), field.isConstant(),
                    field.getDefaultValue() != null, field.isStatic());
        }
        fieldDCL.generateCode(this, null, classWriter, null, null, null);
    }
}

