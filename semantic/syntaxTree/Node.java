package semantic.syntaxTree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public abstract class Node {
    public static String outputPath = "compile/";
    public abstract void generateCode(ClassVisitor cv, MethodVisitor mv);
}
