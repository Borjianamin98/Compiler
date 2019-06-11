package semantic.syntaxTree.expression.call;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.MethodDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.declaration.method.Argument;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodCall extends Expression implements BlockCode {
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
                throw new RuntimeException("Function " + methodName + " is not declared");
            methodDSCP = (MethodDSCP) fetchedDSCP.get();
        }
        return methodDSCP;
    }

    private String getMethodUserDescriptor() {
        return Utility.createMethodCallDescriptor(methodName, parameters);
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getTypeDSCP();
        List<List<Argument>> argumentsDSCP = getTypeDSCP().getAllArguments();
        List<List<Argument>> collectedArguments = argumentsDSCP.stream().filter(arguments -> arguments.size() == parameters.size()).collect(Collectors.toList());
        if (collectedArguments.isEmpty())
            throw new RuntimeException(String.format("No suitable method found for %s: actual and formal argument lists differ in length",
                    getMethodUserDescriptor()));

        // Give rank to each potential method
        List<MethodRank> methodRanks = new ArrayList<>();
        outer:
        for (int indexOfOverload = 0; indexOfOverload < collectedArguments.size(); indexOfOverload++) {
            MethodRank methodRank = new MethodRank(collectedArguments.get(indexOfOverload), indexOfOverload);
            for (int i = 0; i < parameters.size(); i++) {
                TypeDSCP parameterType = parameters.get(i).getResultType();
                TypeDSCP argumentType = collectedArguments.get(indexOfOverload).get(i).getType();
                if (TypeTree.canWiden(parameterType, argumentType)) {
                    methodRank.addSumOfDiffLevel(TypeTree.diffLevel(parameterType, argumentType));
                } else
                    continue outer; // this method is not possible to call
            }
            methodRanks.add(methodRank);
        }
        if (methodRanks.isEmpty())
            throw new RuntimeException(String.format("No suitable method found for %s: argument mismatch", getMethodUserDescriptor()));
        methodRanks.sort(MethodRank.comparator);

        // Extract method which is chosen among overloaded method
        int indexOfOverloadMethod = methodRanks.get(0).getOverloadMethodIndex();
        List<Argument> overloadMethodArguments = methodRanks.get(0).getArguments();

        // Generate call expression code and do type conversion
        for (int i = 0; i < parameters.size(); i++) {
            Expression parameter = parameters.get(i);
            parameter.generateCode(currentClass, currentMethod, cv, mv);
            TypeTree.widen(mv, parameter.getResultType(), overloadMethodArguments.get(i).getType());
        }

        // invoke method
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, getTypeDSCP().getOwner(), getTypeDSCP().getName(),
                methodDSCP.getDescriptor(indexOfOverloadMethod), false);
    }
}
