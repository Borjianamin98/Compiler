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
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class ArrayVariable extends Variable {
    private Variable parent;
    private Expression requestedDimension;
    private HasTypeDSCP parentDSCP;
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
        if (parentDSCP == null) {
            if (!(parent.getDSCP().getType() instanceof ArrayTypeDSCP))
                throw new IllegalTypeException("variable " + parent.getDSCP().getName() + " is not a array");
            arrayTypeDSCP = (ArrayTypeDSCP) parent.getDSCP().getType();
            String parentNameWithDimension = parent.getDSCP().getName();
            String parentName = parentNameWithDimension.substring(0, parentNameWithDimension.lastIndexOf("$"));
            if (arrayTypeDSCP.getInternalType() instanceof ArrayTypeDSCP) {
                int currentParentDimension = Integer.valueOf(parentNameWithDimension.substring(parentNameWithDimension.lastIndexOf("$") + 1));
                Optional<DSCP> fetchedDSCP = Display.find(parentName + "$" + (currentParentDimension + 1));
                if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof HasTypeDSCP))
                    throw new SymbolNotFoundException(parentName + "$" + (currentParentDimension + 1) + " is not declared");
                parentDSCP = (HasTypeDSCP) fetchedDSCP.get();
            } else {
                // TODO think about const array
                parentDSCP = new VariableDSCP(parentName + "$$", arrayTypeDSCP.getInternalType(),
                        arrayTypeDSCP.getInternalType().getSize(), -1, false, true);
            }
        }
        return parentDSCP;
    }
}
