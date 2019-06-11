package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.assignment.DirectAssignment;

import java.util.Optional;

public class AutoVariableDCL extends Declaration {
    private TypeDSCP typeDSCP;
    private Expression defaultValue;

    public AutoVariableDCL(String name, boolean isConstant, Expression defaultValue) {
        super(name, isConstant);
        this.defaultValue = defaultValue;
    }

    public TypeDSCP getTypeDSCP() {
        return defaultValue.getResultType();
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(getName()))
            throw new RuntimeException(getName() + " declared more than one time");

        getTypeDSCP();
        VariableDSCP variableDSCP = new VariableDSCP(getName(), getTypeDSCP(), getTypeDSCP().getSize(),
                top.getFreeAddress(), isConstant(), true);
        top.addSymbol(getName(), variableDSCP);
        new DirectAssignment(new SimpleVariable(getName()), defaultValue).generateCode(currentClass, currentMethod, cv, mv);
    }
}
