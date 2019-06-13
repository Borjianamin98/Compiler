package semantic.syntaxTree.program;

import exception.DuplicateDeclarationException;
import org.objectweb.asm.*;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.syntaxTree.ClassCode;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.record.*;

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
        // We add a scanner field for each class
        classCodes.add(0, new ScannerField());
        for (ClassCode classCode : classCodes) {
            if (classCode instanceof Field) {
                Field field = (Field) classCode;
                field.createFieldDCL(name).generateCode(currentClass, null, classWriter, null, null, null);
                if (field.hasDefaultValue())
                    fields_need_initialized.add(field);
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
        for (Field field : fields_need_initialized) {
            if (!field.isStatic())
                field.generateCode(getName(), classWriter, methodVisitor);
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
            if (field.isStatic())
                field.generateCode(getName(), classWriter, methodVisitor);
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
            throw new DuplicateDeclarationException(name);
        }
    }

    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder(getName()).append('\n');
        for (ClassCode classCode : classCodes) {
            classCode.getCodeRepresentation();
            represent.append('\t').append('\t').append(classCode.getCodeRepresentation()).append('\n');
        }
        return represent.toString();
    }
}

