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
import semantic.symbolTable.descriptor.RecordTypeDSCP;
import semantic.symbolTable.descriptor.TypeDSCP;
import semantic.symbolTable.descriptor.VariableDSCP;
import semantic.syntaxTree.expression.Expression;

import java.util.Optional;

public class VariableDCL extends Declaration {

    public VariableDCL(String name, String type, boolean isConstant, Expression defaultValue) {
        super(name, type, isConstant, defaultValue);
    }

    public VariableDCL(String name, String type, boolean isConstant) {
        super(name, type, isConstant, null);
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(getName())) {
            throw new DuplicateDeclarationException(getName() + " declared more than one time");
        }
        Optional<DSCP> typeDSCP = Display.find(getType());
        if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
            throw new SymbolNotFoundException();
        TypeDSCP variableTypeDSCP = (TypeDSCP) typeDSCP.get();
        if (variableTypeDSCP instanceof RecordTypeDSCP) {
            RecordTypeDSCP recordTypeDSCP = (RecordTypeDSCP) variableTypeDSCP;
            mv.visitTypeInsn(Opcodes.NEW, recordTypeDSCP.getName());
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, recordTypeDSCP.getName(), "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, top.getFreeAddress());
        }
        VariableDSCP variableDSCP = new VariableDSCP(getName(), variableTypeDSCP, variableTypeDSCP.getSize(), top.getFreeAddress(), isConstant());
        top.addSymbol(getName(), variableDSCP);
        if (getDefaultValue() != null) {
            getDefaultValue().generateCode(cv, mv);
            mv.visitVarInsn(Utility.getOpcode(getDefaultValue().getResultType().getTypeCode(), "STORE"), variableDSCP.getAddress());
        }
    }
}
