package semantic.syntaxTree.statement.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.Constants;
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
        switch (condition.getResultType().getTypeCode()) {
            case Constants.INTEGER_CODE:
                // condition can be type int
                break;
            case Constants.LONG_CODE:
            case Constants.FLOAT_CODE:
            case Constants.DOUBLE_CODE:
            case Constants.CHAR_CODE:
            case Constants.STRING_CODE:
                // TODO handle string like python
                break;
            default:
                    throw new BooleanExpressionException();
        }
        Label outLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, outLabel);

        // Uncomment if each for body is a new scope
//        Display.add(true);
        body.generateCode(cv, mv);
//        Display.pop();

        if (stepAssignment != null)
            stepAssignment.generateCode(cv, mv);
        if (stepExpression != null)
            stepExpression.generateCode(cv, mv);
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }
}
