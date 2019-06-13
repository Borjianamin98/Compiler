package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.ArrayDSCP;
import semantic.symbolTable.descriptor.hastype.FieldDSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

/**
 * this class wrap a simple variable name, but depend on
 * type of variable, create SimpleFieldVariable or SimpleLocalVariable internally
 * and forward its call to it
 */
public class SimpleVariableFactory extends Variable {
    private String name;
    private boolean isStatic;
    private Variable internalVariable;

    public SimpleVariableFactory(String name, boolean isStatic) {
        super(name);
        this.name = name;
        this.isStatic = isStatic;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        if (internalVariable == null)
            getDSCP();

        internalVariable.generateCode(currentClass, currentMethod, cv, mv, breakLabel, continueLabel);
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        if (internalVariable == null)
            getDSCP();

        internalVariable.assignValue(currentClass, currentMethod, cv, mv, value);
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (internalVariable == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new RuntimeException(getName() + " is not declared");
            DSCP dscp = fetchedDSCP.get();
            // replace array DSCP with its base type DSCP
            if (dscp instanceof ArrayDSCP)
                dscp = ((ArrayDSCP) dscp).getBaseDSCP();

            if (dscp instanceof FieldDSCP) {
                internalVariable = new SimpleFieldVariable(name, isStatic);
            } else if (dscp instanceof VariableDSCP) {
                internalVariable = new SimpleLocalVariable(name);
            } else
                throw new RuntimeException(getName() + " is not a variable/field");
        }
        return internalVariable.getDSCP();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCodeRepresentation() {
        return name;
    }
}
