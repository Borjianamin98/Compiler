package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
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
     * <p>
     * this method called when assign a value to a variable
     *
     * @param value value which is assign to variable
     */
    protected void setInitializationOfArray(Expression value) {
        String varChainName = getChainName();
        // variable chain name can be something like: a[][].b[][].c[][][]....
        // the most important part for us is after last member access or if it doesn't
        // have any member access, whole of it. for example for a[][].b[], part b[] is
        // most important because it is stored in different symbolTable in descriptor
        // of Record, however for a[][], we want a[][] because it is stored on main program
        // symbol tables (Display contain all of them)
        String varName = varChainName.substring(varChainName.lastIndexOf('.') + 1);

        if (value instanceof NewArrayInstruction) {
            // variable type is a array type otherwise fails
            NewArrayInstruction newArray = (NewArrayInstruction) value;
            ArrayTypeDSCP varTypeDSCP = (ArrayTypeDSCP) getDSCP().getType();

            // find descriptor of variables depend on type variable and place of it's symbolTable
            Map<Integer, HasTypeDSCP> allMap = null;
            if (this instanceof SimpleLocalVariable || this instanceof SimpleFieldVariable) {
                allMap = Display.findAll(Display.findSymbolTable(varName).get(), varName);
            } else if (this instanceof MemberVariable) {
                allMap = Display.findAll(((MemberVariable) this).getRecordTypeDSCP().getSymbolTable(), varName);
            } else if (this instanceof ArrayVariable) {
                RecordTypeDSCP parentRecordDSCP = ((ArrayVariable) this).getParentRecordDSCP();
                if (parentRecordDSCP == null)
                    allMap = Display.findAll(Display.findSymbolTable(varName).get(), varName);
                else
                    allMap = Display.findAll(parentRecordDSCP.getSymbolTable(), varName);
            }

            // always by creating a new array, all of its dimensions will be initialized
            // (if last dimension is a reference type, it will null and uninitialized)
            int currentDimension = varTypeDSCP.getDimensions();
            int initializedDimension = currentDimension - newArray.getDimensionsCount();
            for (int i = currentDimension; i >= initializedDimension; i--) {
                assert allMap != null;
                if (!allMap.containsKey(i))
                    throw new RuntimeException(getChainName() + String.join("", Collections.nCopies(i - currentDimension, "[]")) +
                            " is not declared");
                HasTypeDSCP variableDSCP = allMap.get(i);
                // initialize it if it is not last dimension or if it is not a reference type
                if (i > initializedDimension || !Utility.isReferenceType(variableDSCP.getType())) {
                    variableDSCP.setInitialized(true);
                }
            }
        }
    }
}
