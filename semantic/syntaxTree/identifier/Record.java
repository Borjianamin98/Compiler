package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.expression.Expression;

public class Record extends Expression {
    private String name;


    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {

    }
}
