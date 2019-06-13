package semantic.syntaxTree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Node implements HasRepresentation {
    public static String outputPath = "compile/";

    public static void init() {
        try {
            if (!Files.exists(Paths.get(outputPath)))
                Files.createDirectory(Paths.get(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used to create code of Node for current class and method which wrap this Node
     * if code of Node can contains return and continue code (like code od block, ...) then maybe continueLabel
     * and breakLabel of outer Node is necessary for creating code of Node. They are provided by Node who call
     * generateCode of this node.
     * If breakLabel or continueLabel are null, its mean that they are not provided because it is illegal to use
     * them in this context, So if this node contains break or continue, it may throw exception because it is illegal
     * to have them in this context.
     *
     * @param currentClass current class
     * @param currentMethod current method
     * @param cv current class visitor
     * @param mv current method visitor
     * @param breakLabel break label of enclosed code
     * @param continueLabel continue label of enclosed code
     */
    public abstract void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel);
}
