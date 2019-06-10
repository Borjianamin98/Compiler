package semantic.syntaxTree.declaration.record;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RecordTypeDCL extends Declaration {
    private RecordTypeDSCP typeDSCP;
    private List<Field> fields;

    public RecordTypeDCL(String name, List<Field> fields) {
        super(name, false);
        this.fields = fields;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, getName(), null, "java/lang/Object", null);

        // Record symbol table
        Display.add(false);
        for (Field field : fields) {
            field.setStatic(false);
            Declaration fieldDCL;
            if (field.isArray()) {
                fieldDCL = new ArrayFieldDCL(getName(), field.getName(), field.getBaseType(), field.getDimensions(),
                        field.isConstant(), field.getDefaultValue() != null, field.isStatic());
            } else {
                fieldDCL = new SimpleFieldDCL(getName(), field.getName(), field.getBaseType(), field.isConstant(),
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
                TypeTree.widen(mv, field.getDefaultValue().getResultType(), field.getType()); // right value must be converted to type of variable
                methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, getName(), field.getName(), field.getDescriptor());
            }
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
            throw new DuplicateDeclarationException("Identifier " + getName() + " declared more than one time");
        }
        RecordTypeDSCP recordDSCP = new RecordTypeDSCP(getName(), 1, Display.pop());
        SymbolTable.addType(getName(), recordDSCP);
    }
}
