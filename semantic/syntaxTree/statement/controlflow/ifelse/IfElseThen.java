package semantic.syntaxTree.statement.controlflow.ifelse;

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

public class IfElseThen extends Statement {
    private Expression condition;
    private Block ifBody;
    private Block elseBody;

    public IfElseThen(Expression condition, Block ifBody, Block elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    private void generateBodyCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                                  Label breakLabel, Label continueLabel, Block code) {
        for (BlockCode blockCode : code.getBlockCodes()) {
            blockCode.generateCode(currentClass, currentMethod, cv, mv, breakLabel, continueLabel);
            if (blockCode instanceof ReturnStatement)
                break; // other code in this block are unnecessary
        }
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        Label elseLabel = new Label();
        Label outLabel = new Label();

        // Generate condition
        Utility.evaluateBooleanExpressionFalse(currentClass, currentMethod, cv, mv, condition, elseLabel);

        // generate body code
        Display.add(true);
        if (ifBody != null) {
            generateBodyCode(currentClass, currentMethod, cv, mv, breakLabel, continueLabel, ifBody);
        }
        Display.pop();
        mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        mv.visitLabel(elseLabel);
        Display.add(true);
        if (elseBody != null) {
            generateBodyCode(currentClass, currentMethod, cv, mv, breakLabel, continueLabel, elseBody);
        }
        Display.pop();

        mv.visitLabel(outLabel);
    }

    public String getCodeRepresentation() {
        return "if (" + condition.getCodeRepresentation() + ") begin ... end";
    }
}
