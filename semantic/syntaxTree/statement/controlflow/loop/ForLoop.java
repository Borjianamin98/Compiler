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
import semantic.syntaxTree.statement.assignment.Assignment;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

public class ForLoop extends Statement {
    private Assignment initialAssignment;
    private Expression condition;
    /**
     * only one of steps available at any time
     */
    private Assignment stepAssignment;
    private Expression stepExpression;

    private Block body;

    public ForLoop(Assignment initialAssignment, Expression condition, Assignment stepAssignment, Block body) {
        this.initialAssignment = initialAssignment;
        this.condition = condition;
        this.stepAssignment = stepAssignment;
        this.body = body;
    }

    public ForLoop(Assignment initialAssignment, Expression condition, Expression stepExpression, Block body) {
        this.initialAssignment = initialAssignment;
        this.condition = condition;
        this.stepExpression = stepExpression;
        this.body = body;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // Generate initial statement
        if (initialAssignment != null)
            initialAssignment.generateCode(currentClass, currentMethod, cv, mv);

        Label conditionLabel = new Label();
        Label stepLabel = new Label();
        Label outLabel = new Label();

        // Generate condition
        mv.visitLabel(conditionLabel);
        Utility.evaluateBooleanExpressionFalse(currentClass, currentMethod, cv, mv, condition, outLabel);

        // generate body code
        Display.add(true);
        if (body != null) {
            for (BlockCode blockCode : body.getBlockCodes()) {
                if (blockCode instanceof BreakStatement) {
                    mv.visitJumpInsn(Opcodes.GOTO, outLabel);
                    break; // other code in this block are unnecessary
                } else if (blockCode instanceof ContinueStatement) {
                    mv.visitJumpInsn(Opcodes.GOTO, stepLabel);
                    break; // other code in this block are unnecessary
                } else if (blockCode instanceof ReturnStatement) {
                    blockCode.generateCode(currentClass, currentMethod, cv, mv);
                    break; // other code in this block are unnecessary
                } else
                    blockCode.generateCode(currentClass, currentMethod, cv, mv);
            }
        }
        Display.pop();

        // generate step assigment
        mv.visitLabel(stepLabel);
        if (stepAssignment != null)
            stepAssignment.generateCode(currentClass, currentMethod, cv, mv);
        if (stepExpression != null)
            stepExpression.generateCode(currentClass, currentMethod, cv, mv);

        // generate jump for for-loop
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }
}
