package semantic.syntaxTree.statement.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Constants;
import semantic.exception.BooleanExpressionException;
import semantic.symbolTable.Display;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.statement.assignment.Assignment;

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
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        if (initialAssignment != null)
            initialAssignment.generateCode(cv, mv);
        Label conditionLabel = new Label();
        mv.visitLabel(conditionLabel);
        condition.generateCode(cv, mv);
        int resultTypeCode = condition.getResultType().getTypeCode();
        if (resultTypeCode == Constants.INTEGER_DSCP.getTypeCode()) {
            // condition can be type int
        } else if (resultTypeCode == Constants.LONG_DSCP.getTypeCode() ||
                resultTypeCode == Constants.FLOAT_DSCP.getTypeCode() ||
                resultTypeCode == Constants.DOUBLE_DSCP.getTypeCode() ||
                resultTypeCode == Constants.CHAR_DSCP.getTypeCode() ||
                resultTypeCode == Constants.STRING_DSCP.getTypeCode()) {
            // TODO handle string like python
        } else {
            throw new BooleanExpressionException();
        }
        Label outLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, outLabel);

        Display.add(true);
        body.generateCode(cv, mv);
        Display.pop();

        if (stepAssignment != null)
            stepAssignment.generateCode(cv, mv);
        if (stepExpression != null)
            stepExpression.generateCode(cv, mv);
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }
}
