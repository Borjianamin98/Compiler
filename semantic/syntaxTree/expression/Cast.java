package semantic.syntaxTree.expression;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import semantic.exception.IllegalTypeException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.program.ClassDCL;
import semantic.typeTree.TypeTree;

import java.util.Optional;

public class Cast extends Expression {
    private String castType;
    private SimpleTypeDSCP castTypeDSCP;
    private Expression operand;

    public Cast(String castType, Expression operand) {
        this.castType = castType;
        this.operand = operand;
    }

    @Override
    public SimpleTypeDSCP getResultType() {
        if (castTypeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(castType);
            if (!fetchedDSCP.isPresent())
                throw new SymbolNotFoundException(castType + " is not declared");
            if (fetchedDSCP.get() instanceof SimpleTypeDSCP) {
                castTypeDSCP = (SimpleTypeDSCP) fetchedDSCP.get();
            } else
                throw new IllegalTypeException(castType + " is not a primitive type");
        }
        return castTypeDSCP;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        operand.generateCode(currentClass, currentMethod, cv, mv);
        try {
            // maybe a implicit (widen) cast is enough
            TypeTree.widen(mv, operand.getResultType(), getResultType());
        } catch (RuntimeException ex) {
            // otherwise do explicit (narrow) cast
            TypeTree.narrow(mv, operand.getResultType(), getResultType());
        }
    }
}
