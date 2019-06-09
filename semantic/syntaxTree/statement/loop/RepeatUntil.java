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

public class RepeatUntil extends Statement {
    private Expression condition;
    private Block body;

    public RepeatUntil(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        Label bodyLabel = new Label();
        mv.visitLabel(bodyLabel);

        Display.add(true);
        body.generateCode(cv, mv);
        Display.pop();

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
        mv.visitJumpInsn(Opcodes.IFNE, bodyLabel);
    }
}
