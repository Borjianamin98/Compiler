package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.exception.DuplicateDeclarationException;
import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class VariableDCL extends Declaration {
    public VariableDCL(String name, String type, boolean isConstant) {
        super(name, type, isConstant);
    }

    @Override
    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getType());
            if (!fetchedDSCP.isPresent() || !(fetchedDSCP.get() instanceof TypeDSCP))
                throw new SymbolNotFoundException("Type " + getType() + " not found");
            typeDSCP = (TypeDSCP) fetchedDSCP.get();
        }
        return typeDSCP;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(getName())) {
            throw new DuplicateDeclarationException(getName() + " declared more than one time");
        }
        getTypeDSCP();
//        if (getTypeDSCP() instanceof RecordTypeDSCP) {
//            RecordTypeDSCP recordTypeDSCP = (RecordTypeDSCP) getTypeDSCP();
//            mv.visitTypeInsn(Opcodes.NEW, recordTypeDSCP.getName());
//            mv.visitInsn(Opcodes.DUP);
//            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, recordTypeDSCP.getName(), "<init>", "()V", false);
//            mv.visitVarInsn(Opcodes.ASTORE, top.getFreeAddress());
//            isInitialized = true;
//        }
        VariableDSCP variableDSCP = new VariableDSCP(getName(), getTypeDSCP(), getTypeDSCP().getSize(), top.getFreeAddress(), isConstant(), false);
        top.addSymbol(getName(), variableDSCP);
    }
}
