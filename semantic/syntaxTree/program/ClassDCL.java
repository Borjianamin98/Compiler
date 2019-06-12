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
import java.util.List;
import java.util.Optional;

public class ClassDCL extends Node {
    private String name;
    private List<ClassCode> classCodes;

    public ClassDCL(String name) {
        this.name = name;
        this.classCodes = new ArrayList<>();
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

        for (ClassCode classCode : classCodes) {
            if (classCode instanceof Field)
                createFieldCode(classWriter, methodVisitor, (Field) classCode);
            else if (classCode instanceof MethodDCL || classCode instanceof RecordTypeDCL)
                ((Declaration) classCode).generateCode(this, null, classWriter, null, null, null);
        }

        // Check all method declaration are provided (not prototype)
        for (ClassCode classCode : classCodes) {
            if (classCode instanceof MethodDCL)
            {
                MethodDCL methodDCL = (MethodDCL) classCode;
                Optional<DSCP> dscp = Display.find(methodDCL.getName());
                if (!dscp.isPresent() || !(dscp.get() instanceof MethodDSCP))
                    throw new AssertionError("doesn't happen");
                ((MethodDSCP) dscp.get()).checkAllSignaturesDeclared();
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
            throw new RuntimeException("Identifier " + name + " declared more than one time");
        }
    }

    private void createFieldCode(ClassWriter classWriter, MethodVisitor methodVisitor, Field field) {
        // Create field
        Declaration fieldDCL;
        if (field.isArray()) {
            fieldDCL = new ArrayFieldDCL(name, field.getName(), field.getBaseType(), field.getDimensions(),
                    field.isConstant(), field.getDefaultValue() != null, false);
        } else {
            fieldDCL = new SimpleFieldDCL(name, field.getName(), field.getBaseType(), field.isConstant(),
                    field.getDefaultValue() != null, false);
        }
        fieldDCL.generateCode(this, null, classWriter, null, null, null);

        // initialize field
        if (field.getDefaultValue() != null) {
            if (field.isStatic()) {
                field.getDefaultValue().generateCode(this, null, classWriter, methodVisitor, null, null);
                methodVisitor.visitFieldInsn(Opcodes.PUTSTATIC, name, field.getName(), field.getDescriptor());
            } else {
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0); // load "this"
                field.getDefaultValue().generateCode(this, null, classWriter, methodVisitor, null, null);
                methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, name, field.getName(), field.getDescriptor());
            }
        }
    }
}

