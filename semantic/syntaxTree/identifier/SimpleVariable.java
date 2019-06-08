package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.Display;
import semantic.syntaxTree.expression.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleVariable extends Variable {
    private VariableDSCP dscp;
    private List<Expression> dimensions;
    private String name;

    public SimpleVariable(String name) {
        this.name = name;
        this.dimensions = new ArrayList<>();
    }

    public SimpleVariable(String name, List<Expression> dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }

    public void generateDimensionCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (dscp.isArray()) {
            if (dscp.getArrayLevel() != getDimensions().size())
                throw new RuntimeException(getName() + " is a " + dscp.getArrayLevel() + "-dimension array");
            // load all dimension except last dimension
            mv.visitVarInsn(Opcodes.ALOAD, dscp.getAddress());
            for (int i = 0; i < getDimensions().size() - 1; i++) {
                Expression dimension = getDimensions().get(i);
                dimension.generateCode(cv, mv);
                mv.visitInsn(Opcodes.AALOAD);
            }
        }
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        if (dscp.isArray()) {
            generateDimensionCode(cv, mv);
            getDimensions().get(getDimensions().size() - 1).generateCode(cv, mv);
            mv.visitInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "ALOAD"));
        } else {
            if (!dscp.isInitialized())
                throw new RuntimeException("Variable " + getName() + " is not initialized");
            mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "LOAD"), dscp.getAddress());
        }
        setResultType(dscp.getType());
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        if (dscp.isArray()) {
            generateDimensionCode(cv, mv);
            getDimensions().get(getDimensions().size() - 1).generateCode(cv, mv);
            value.generateCode(cv, mv);
            mv.visitInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "ASTORE"));
        } else {
            value.generateCode(cv, mv);
            mv.visitVarInsn(Utility.getOpcode(dscp.getType().getTypeCode(), "STORE"), dscp.getAddress());
        }
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(getName() + " is not declared");
            if (fetchedDSCP.get() instanceof VariableDSCP) {
                dscp = (VariableDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(getName() + " is not a hastype");
        }
        return dscp;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getDimensions() {
        return dimensions;
    }
}
