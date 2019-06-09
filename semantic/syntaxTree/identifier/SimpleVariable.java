package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class SimpleVariable extends Variable {
    private VariableDSCP dscp;
    private String name;

    public SimpleVariable(String name) {
        this.name = name;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (!dscp.isInitialized())
            throw new RuntimeException("Variable " + getName() + " is not initialized");
        mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "LOAD"), dscp.getAddress());
    }

    @Override
    public void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        value.generateCode(currentClass, currentMethod, cv, mv);
        TypeTree.widen(mv, value.getResultType(), getResultType()); // right value must be converted to type of variable
        mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "STORE"), dscp.getAddress());
    }

    @Override
    public VariableDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(getName() + " is not declared");
            if (fetchedDSCP.get() instanceof VariableDSCP) {
                dscp = (VariableDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(getName() + " is not a variable");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }

}
