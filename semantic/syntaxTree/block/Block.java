package semantic.syntaxTree.block;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class Block extends Node {
    private List<Statement> statements;

    public Block() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        for (Statement statement : statements) {
            statement.generateCode(cv, mv);
        }
    }
}
