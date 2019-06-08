package semantic.syntaxTree.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class ArrayVariable extends Variable {
    private Variable parent;
    private Expression requestedDimension;
    private HasTypeDSCP dscp;
    private ArrayTypeDSCP arrayTypeDSCP;

    public ArrayVariable(Variable parent, Expression requestedDimension) {
        this.parent = parent;
        this.requestedDimension = requestedDimension;
    }

    @Override
    public void assignValue(ClassVisitor cv, MethodVisitor mv, Expression value) {
        getDSCP();
        parent.generateCode(cv, mv);
        requestedDimension.generateCode(cv, mv);
        value.generateCode(cv, mv);
        mv.visitInsn(Utility.getOpcode(arrayTypeDSCP.getInternalType().getTypeCode(), "ASTORE"));
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        getDSCP();
        parent.generateCode(cv, mv);
        requestedDimension.generateCode(cv, mv);
        mv.visitInsn(Utility.getOpcode(arrayTypeDSCP.getInternalType().getTypeCode(), "ALOAD"));
        setResultType(arrayTypeDSCP.getInternalType());
    }

    @Override
    public HasTypeDSCP getDSCP() {
        if (dscp == null) {
            if (!(parent.getDSCP().getType() instanceof ArrayTypeDSCP))
                throw new IllegalTypeException("Variable " + parent.getDSCP().getName() + " is not a array");
            arrayTypeDSCP = (ArrayTypeDSCP) parent.getDSCP().getType();
            Optional<DSCP> fetchedDSCP = Display.find(parent.getDSCP().getName() + "[]");
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof HasTypeDSCP))
                throw new SymbolNotFoundException(parent.getDSCP().getName() + "[]" + " is not declared");
            dscp = (HasTypeDSCP) fetchedDSCP.get();
        }
        return dscp;
    }
}
