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

public class ForLoop extends Statement {
    private Statement initialAssignment;
    private Expression condition;
    /**
     * only one of steps available at any time
     */
    private Statement stepAssignment;
    private Expression stepExpression;

    private Block body;

    public ForLoop(Statement initialAssignment, Expression condition, Statement stepAssignment, Block body) {
        this.initialAssignment = initialAssignment;
        this.condition = condition;
        this.stepAssignment = stepAssignment;
        this.body = body;
    }

    public ForLoop(Statement initialAssignment, Expression condition, Expression stepExpression, Block body) {
        this.initialAssignment = initialAssignment;
        this.condition = condition;
        this.stepExpression = stepExpression;
        this.body = body;
    }

    public ForLoop(Statement initialAssignment, Expression condition, Block body) {
        this.initialAssignment = initialAssignment;
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        // Generate initial statement
        if (initialAssignment != null)
            initialAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);

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
                blockCode.generateCode(currentClass, currentMethod, cv, mv, outLabel, stepLabel);
                if (blockCode instanceof ReturnStatement ||
                        blockCode instanceof BreakStatement ||
                        blockCode instanceof ContinueStatement)
                    break; // other code in this block are unnecessary
            }
        }
        Display.pop();


        // generate step assigment
        mv.visitLabel(stepLabel);
        if (stepAssignment != null)
            stepAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);
        if (stepExpression != null) {
            stepExpression.generateCode(currentClass, currentMethod, cv, mv, null, null);
            mv.visitInsn(Opcodes.POP);
        }

        // generate jump for for-loop
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }

    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        represent.append("for (");
        if (initialAssignment != null)
            represent.append(initialAssignment.getCodeRepresentation());
        represent.append("; ");
        represent.append(condition.getCodeRepresentation()).append("; ");
        if (stepAssignment != null)
            represent.append(stepAssignment.getCodeRepresentation());
        if (stepExpression != null)
            represent.append(stepExpression.getCodeRepresentation());
        represent.append(") begin .. end");
        return represent.toString();
    }
}
