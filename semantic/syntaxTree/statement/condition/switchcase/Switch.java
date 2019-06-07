package semantic.syntaxTree.statement.condition.switchcase;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.statement.Statement;

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

    public Switch(Expression expression, List<Case> cases) {
        this(expression, cases, null);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        expression.generateCode(cv, mv);
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
            aCase.getBlock().generateCode(cv, mv);
            mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        }
        mv.visitLabel(defaultLabel);
        if (defaultCase != null)
            defaultCase.generateCode(cv, mv);
        mv.visitLabel(outLabel);
    }
}
