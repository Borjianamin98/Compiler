package semantic.syntaxTree.statement.controlflow.switchcase;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

import java.util.Comparator;
import java.util.List;

public class Switch extends Statement {
    private Expression expression;
    private List<Case> cases;
    private Block defaultCase;

    public Switch(Expression expression, List<Case> cases, Block defaultCase) {
        this.expression = expression;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    private void generateCaseCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Block caseBlock, Label outLabel) {
        for (BlockCode blockCode : caseBlock.getBlockCodes()) {
            if (blockCode instanceof BreakStatement) {
                mv.visitJumpInsn(Opcodes.GOTO, outLabel);
                break; // other code in this block are unnecessary
            } else if (blockCode instanceof ContinueStatement)
                throw new RuntimeException("Continue outside of loop");
            else if (blockCode instanceof ReturnStatement) {
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
                break; // other code in this block are unnecessary
            } else
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
        }
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        expression.generateCode(currentClass, currentMethod, cv, mv);
        Label defaultLabel = new Label();
        Label outLabel = new Label();
        Label[] caseLabels = new Label[cases.size()];
        for (int i = 0; i < cases.size(); i++)
            caseLabels[i] = new Label();
        cases.sort((Comparator.comparing(Case::getNumber)));
        int[] keys = cases.stream().mapToInt(Case::getNumber).toArray();
        mv.visitLookupSwitchInsn(defaultLabel, keys, caseLabels);
        for (int i = 0; i < cases.size(); i++) {
            Case aCase = cases.get(i);
            Label caseLable = caseLabels[i];
            mv.visitLabel(caseLable);
            generateCaseCode(currentClass, currentMethod, cv, mv, aCase.getBlock(), outLabel);
            mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        }
        mv.visitLabel(defaultLabel);
        if (defaultCase != null)
            generateCaseCode(currentClass, currentMethod, cv, mv, defaultCase, outLabel);
        mv.visitLabel(outLabel);
    }
}
