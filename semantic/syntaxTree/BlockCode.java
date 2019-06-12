package semantic.syntaxTree;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;

/**
 * a block can contain statement however some of classes are not of type
 * statement. interface BlockCode means that Node can be used in Block.
 * a Block contains a list of BlockCode and each statement is BlockCode
 * (Statement implements BlockCode)
 */
public interface BlockCode {
    void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                      Label breakLabel, Label continueLabel);
}
