package semantic.syntaxTree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Node {
    public static String outputPath = "compile/";

    static {
        try {
            if (!Files.exists(Paths.get(outputPath)))
                Files.createDirectory(Paths.get(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv);
}
