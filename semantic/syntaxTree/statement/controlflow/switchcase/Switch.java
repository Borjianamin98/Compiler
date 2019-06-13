package semantic.syntaxTree.statement.controlflow.switchcase;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.symbolTable.typeTree.TypeTree;

import java.util.Comparator;
import java.util.List;

public class Switch extends Statement {
    private Expression expression;
    private List<Case> cases;
    private DefaultCase defaultCase;

    public Switch(Expression expression, List<Case> cases, DefaultCase defaultCase) {
        this.expression = expression;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv,
                             Label breakLabel, Label continueLabel) {
        // generate switch expression
        expression.generateCode(currentClass, currentMethod, cv, mv, null, null);
        // switch case can only work for integer type (not even long type)
        TypeTree.widen(mv, expression.getResultType(), TypeTree.INTEGER_DSCP);

        Label defaultLabel = new Label();
        Label outLabel = new Label();

        // generate label for cases
        Label[] caseLabels = new Label[cases.size()];
        for (int i = 0; i < cases.size(); i++)
            caseLabels[i] = new Label();
        cases.sort((Comparator.comparing(Case::getNumber)));
        int[] keys = cases.stream().mapToInt(Case::getNumber).toArray();

        // generate case table lookup
        mv.visitLookupSwitchInsn(defaultLabel, keys, caseLabels);

        // generate case code
        for (int i = 0; i < cases.size(); i++) {
            Case aCase = cases.get(i);
            Label caseLabel = caseLabels[i];
            mv.visitLabel(caseLabel);
            aCase.generateCode(currentClass, currentMethod, cv, mv, outLabel, continueLabel);
            mv.visitJumpInsn(Opcodes.GOTO, outLabel);
        }
        mv.visitLabel(defaultLabel);
        if (defaultCase != null)
            defaultCase.generateCode(currentClass, currentMethod, cv, mv, outLabel, continueLabel);
        mv.visitLabel(outLabel);
    }

    @Override
    public String getCodeRepresentation() {
        return "switch (" + expression.getCodeRepresentation() + ") begin ... end";
    }
}
