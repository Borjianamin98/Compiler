package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.VariableDSCP;
import semantic.symbolTable.Display;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class SimpleVariable extends Variable {
    private VariableDSCP dscp;

    public SimpleVariable(String name) {
        super(name);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "LOAD"), dscp.getAddress());
        setResultType(dscp.getType());
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        value.generateCode(cv, mv);
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

}
