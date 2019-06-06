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

public class Variable extends Expression {
    private String name;
    private VariableDSCP dscp;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "LOAD"), dscp.getAddress());
        setResultType(dscp.getType());
    }

    public VariableDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(name);
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(name + " is not declared");
            if (fetchedDSCP.get() instanceof VariableDSCP) {
                dscp = (VariableDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(name + " is not a variable");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }
}
