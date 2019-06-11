package semantic.syntaxTree.statement.controlflow.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

public class RepeatUntil extends Statement {
    private Expression condition;
    private Block body;

    public RepeatUntil(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        Label bodyLabel = new Label();
        Label conditionLabel = new Label();
        Label outLabel = new Label();
        mv.visitLabel(bodyLabel);

        Display.add(true);
        for (BlockCode blockCode : body.getBlockCodes()) {
            if (blockCode instanceof BreakStatement) {
                mv.visitJumpInsn(Opcodes.GOTO, outLabel);
                break; // other code in this block are unnecessary
            } else if (blockCode instanceof ContinueStatement) {
                mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
                break; // other code in this block are unnecessary
            } else if (blockCode instanceof ReturnStatement) {
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
                break; // other code in this block are unnecessary
            } else
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
        }
        Display.pop();

        // generate condition
        mv.visitLabel(conditionLabel);
        Utility.evaluateBooleanExpressionTrue(currentClass, currentMethod, cv, mv, condition, bodyLabel);

        mv.visitLabel(outLabel);
    }
}
