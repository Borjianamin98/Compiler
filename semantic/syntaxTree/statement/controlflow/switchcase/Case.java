package semantic.syntaxTree.statement.controlflow.switchcase;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

public class Case extends Node {
    private int number;
    private Block block;

    public Case(int number, Block block) {
        this.number = number;
        this.block = block;
    }

    public int getNumber() {
        return number;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        for (BlockCode blockCode : block.getBlockCodes()) {
            blockCode.generateCode(currentClass, currentMethod, cv, mv, breakLabel, continueLabel);
            if (blockCode instanceof ReturnStatement ||
                    blockCode instanceof BreakStatement)
                break; // other code in this block are unnecessary
        }
    }

    public String getCodeRepresentation() {
        return "case " + number + ": begin ... end";
    }
}
