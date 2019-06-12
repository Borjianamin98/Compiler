package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.instance.NewArrayInstruction;
import semantic.syntaxTree.program.ClassDCL;

import java.util.Collections;
import java.util.Map;

public abstract class Variable extends Expression {
    /**
     * represent a final name of variable if apply array index or member field on variable
     */
    private String chainName;

    public Variable(String chainName) {
        this.chainName = chainName;
    }

    public abstract void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract HasTypeDSCP getDSCP();

    @Override
    public TypeDSCP getResultType() {
        return getDSCP().getType();
    }

    public String getChainName() {
        return chainName;
    }

    /**
     * check to see if value is a NewArrayInstruction, initialize all
     * var, var[], var[][]... corresponding to dimension of NewArrayInstruction
     *
     * this method called depend on context by implementation of variable
     * @param varName variable name which assign to it
     * @param value value which is assign to variable
     */
    protected void setInitializationOfArray(String varName, Expression value) {
        if (value instanceof NewArrayInstruction) {
            // variable type is a array type otherwise fails
            NewArrayInstruction newArray = (NewArrayInstruction) value;
            ArrayTypeDSCP varTypeDSCP = (ArrayTypeDSCP) getDSCP().getType();

            // always by creating a new array, all of its dimensions will be initialized
            // (if last dimension is a reference type, it will null and uninitialized)
            Map<Integer, VariableDSCP> allMap = Display.findAll(varName);
            int currentDimension = varTypeDSCP.getDimensions();
            int initializedDimension = currentDimension - newArray.getDimensionsCount();
            for (int i = currentDimension; i >= initializedDimension; i--) {
                if (!allMap.containsKey(i))
                    throw new RuntimeException(getChainName() + String.join("", Collections.nCopies(i - currentDimension, "[]")) +
                            " is not declared");
                VariableDSCP variableDSCP = allMap.get(i);
                // initialize it if it is not last dimension or if it is not a reference type
                if (i > initializedDimension || !Utility.isReferenceType(variableDSCP.getType())) {
                    variableDSCP.setInitialized(true);
                }
            }
        }
    }
}
