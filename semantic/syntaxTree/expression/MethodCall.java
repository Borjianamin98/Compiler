package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Constants;
import semantic.exception.SymbolNotFoundException;
import semantic.exception.TypeMismatchException;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.syntaxTree.declaration.method.Argument;

import java.util.List;
import java.util.Optional;

public class MethodCall extends Expression {
    private String methodName;
    private List<Expression> parameters;

    public MethodCall(String methodName, List<Expression> parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        Optional<DSCP> fetchedDSCP = Display.find(methodName);
        if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof MethodDSCP))
            throw new SymbolNotFoundException("Function " + methodName + " is not declared");
        MethodDSCP methodDSCP = (MethodDSCP) fetchedDSCP.get();

        // TODO Choose best overloading method
        List<Argument> argumentsDSCP = methodDSCP.getArguments(0);
//        if (parameters.size() != argumentsDSCP.size())
//            throw new RuntimeException("There is no method " + methodName + " with " + parameters.size() + " arguments");
        for (int i = 0; i < parameters.size(); i++) {
            Expression parameter = parameters.get(i);
            parameter.generateCode(cv, mv);
//            if (parameter.getResultType().getTypeCode() != argumentsDSCP.get(i).getBaseType().getTypeCode())
//                throw new TypeMismatchException((i + 1) + "-th parameter (" + parameter.getResultType().getDescriptor() + ") doesn't match with "
//                        + (i + 1) + "-th argument (" + argumentsDSCP.get(i).getBaseType().getDescriptor() + ") of " + methodName);
        }
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, methodDSCP.getOwner(), methodDSCP.getName(), methodDSCP.getDescriptor(0), false);
        if (methodDSCP.hasReturn())
            setResultType(methodDSCP.getReturnType());
        else
            setResultType(Constants.VOID_DSCP);
    }
}
