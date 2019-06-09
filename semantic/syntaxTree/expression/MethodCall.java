package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.Argument;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.List;
import java.util.Optional;

public class MethodCall extends Expression {
    private String methodName;
    private List<Expression> parameters;
    private MethodDSCP methodDSCP;

    public MethodCall(String methodName, List<Expression> parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public TypeDSCP getResultType() {
        getTypeDSCP();
        if (methodDSCP.hasReturn())
            return methodDSCP.getReturnType();
        else
            return TypeTree.VOID_DSCP;
    }

    private MethodDSCP getTypeDSCP() {
        if (methodDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(methodName);
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof MethodDSCP))
                throw new SymbolNotFoundException("Function " + methodName + " is not declared");
            methodDSCP = (MethodDSCP) fetchedDSCP.get();
        }
        return methodDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getTypeDSCP();
        // TODO Choose best overloading method
        List<Argument> argumentsDSCP = getTypeDSCP().getArguments(0);
//        if (parameters.size() != argumentsDSCP.size())
//            throw new RuntimeException("There is no method " + methodName + " with " + parameters.size() + " arguments");
        for (int i = 0; i < parameters.size(); i++) {
            Expression parameter = parameters.get(i);
            parameter.generateCode(currentClass, currentMethod, cv, mv);
//            if (parameter.getResultType().getTypeCode() != argumentsDSCP.get(i).getBaseType().getTypeCode())
//                throw new TypeMismatchException((i + 1) + "-th parameter (" + parameter.getResultType().getDescriptor() + ") doesn't match with "
//                        + (i + 1) + "-th argument (" + argumentsDSCP.get(i).getBaseType().getDescriptor() + ") of " + methodName);
        }
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, getTypeDSCP().getOwner(), getTypeDSCP().getName(), methodDSCP.getDescriptor(0), false);
    }
}
