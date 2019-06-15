import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import semantic.syntaxTree.HasRepresentation;

@Aspect
public class CompilerAspect {

    @Pointcut("execution(void semantic.syntaxTree.statement.assignment.DirectAssignment.generateCode(..))")
    private void pointDirectAssignment() {}

    @Pointcut("execution(void semantic.syntaxTree.declaration.method.MethodDCL.generateCode(..))")
    private void pointMethodDCL() {}

    @Pointcut("execution(void semantic.syntaxTree.declaration.record.*.generateCode(..))")
    private void pointRecordDCL() {}

    @Pointcut("execution(void semantic.syntaxTree.declaration.VariableDCL.generateCode(..))")
    private void pointVariableDCL() {}

    @Pointcut("execution(void semantic.syntaxTree.declaration.ArrayDCL.generateCode(..))")
    private void poinyArrayDCL() {}

    @Pointcut("execution(void semantic.syntaxTree.program.ClassDCL.generateCode(..))")
    private void pointClassDCL() {}

    @Pointcut("pointMethodDCL() || pointRecordDCL() || pointVariableDCL() || pointClassDCL() || poinyArrayDCL()")
    private void pointDeclaration() {}

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Pointcut("execution(void semantic.syntaxTree.statement.controlflow.*.generateCode(..))")
    private void pointControlFlowStatement() {}

    @Pointcut("execution(void semantic.syntaxTree.statement.controlflow.ifelse.IfElseThen.generateCode(..))")
    private void pointControlFlowIfElse() {}

    @Pointcut("execution(void semantic.syntaxTree.statement.controlflow.loop.*.generateCode(..))")
    private void pointControlFlowLoop() {}

    @Pointcut("execution(void semantic.syntaxTree.statement.controlflow.switchcase.*.generateCode(..))")
    private void pointControlFlowSwitch() {}

    @Pointcut("pointControlFlowStatement() || pointControlFlowIfElse() || pointControlFlowLoop() || pointControlFlowSwitch()")
    private void pointControlFlow() {}

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Pointcut("execution(void semantic.syntaxTree.statement.PrintFunction.generateCode(..))")
    private void pointPrintFunction() {}

    @Pointcut("execution(void semantic.syntaxTree.expression.call.*.generateCode(..))")
    private void pointCall() {}

    @Pointcut("pointPrintFunction() || pointCall()")
    private void pointMethodCall() {}

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Pointcut("execution(void semantic.syntaxTree.expression.operation.relational.*.generateCode(..))")
    private void pointRelational() {}

    @Pointcut("pointRelational()")
    private void pointExpression() {}

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @AfterThrowing("pointDirectAssignment() || pointDeclaration() || pointControlFlow() || pointMethodCall()" +
            " || pointExpression() || pointDeclaration()")
    public void beforeAddAccount(JoinPoint joinPoint) {
        HasRepresentation node = (HasRepresentation) joinPoint.getTarget();
        System.out.println("\t\tat: " + node.getCodeRepresentation());
    }

    @AfterThrowing("execution(* *.getResultType())")
    public void afterGetResultType(JoinPoint joinPoint) {
        HasRepresentation node = (HasRepresentation) joinPoint.getTarget();
        System.out.println("\t\tat: " + node.getCodeRepresentation());
    }
}
