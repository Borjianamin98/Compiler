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

public class DefaultCase extends Node {
    private Block block;

    public DefaultCase(Block block) {
        this.block = block;
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
        return "default : begin ... end";
    }
}
