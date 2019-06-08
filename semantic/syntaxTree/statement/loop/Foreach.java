package semantic.syntaxTree.statement.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.identifier.SimpleVariable;
import semantic.syntaxTree.identifier.Variable;
import semantic.syntaxTree.statement.Statement;

public class Foreach extends Statement {
    private String identifierName;
    private SimpleVariable iterator;
    private Block body;

    public Foreach(String identifierName, SimpleVariable iterator, Block body) {
        this.identifierName = identifierName;
        this.iterator = iterator;
        this.body = body;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
//        iterator.generateCode(cv, mv);
//        if (!(iterator.getDSCP().isArray()))
//            throw new RuntimeException(iterator.getDSCP().getName() + " is not iterable");
//
//        Display.top().getTemp(iterator.getResultType(), iterator.getDSCP().getArrayLevel() - 1);
//        ForLoop forLoop = new ForLoop(null, condition, );
    }
}
