package semantic.syntaxTree.expression.identifier;

import exception.DuplicateDeclarationException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class SimpleLocalVariable extends Variable {
    private VariableDSCP dscp;
    private String name;

    public SimpleLocalVariable(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        getDSCP();
        if (!getDSCP().isInitialized())
            throw new RuntimeException(String.format("Variable %s might not have been initialized", getChainName()));
        mv.visitVarInsn(Utility.getOpcode(dscp.getType(), "LOAD", false), dscp.getAddress());
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (getDSCP().isConstant() && getDSCP().isInitialized())
            throw new RuntimeException(String.format("Cannot assign a value to const variable %s. Variable %s already have been assigned",
                    getChainName(), getChainName()));
        value.generateCode(currentClass, currentMethod, cv, mv, null, null);
        TypeTree.widen(mv, value.getResultType(), getResultType()); // right value must be converted to type of variable
        mv.visitVarInsn(Utility.getOpcode(dscp.getType(), "STORE", false), dscp.getAddress());
        getDSCP().setInitialized(true);
        setInitializationOfArray(value);
    }

    @Override
    public VariableDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new DuplicateDeclarationException(getChainName());
            if (fetchedDSCP.get() instanceof VariableDSCP) {
                dscp = (VariableDSCP) fetchedDSCP.get();
            } else
                throw new RuntimeException(getChainName() + " is not a variable");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }

}
