package semantic.syntaxTree.declaration;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.RecordTypeDSCP;
import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.assignment.DirectAssignment;

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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
        // Only check current block table
        // otherwise this declaration shadows other declarations
        SymbolTable top = Display.top();
        if (top.contain(getName()))
            throw new RuntimeException(getName() + " declared more than one time");

        getTypeDSCP();
        Declaration variable;
        if (defaultValue.getResultType() instanceof SimpleTypeDSCP) {
            variable = new VariableDCL(getName(), getTypeDSCP().getName(), isConstant(), false);
        } else if (defaultValue.getResultType() instanceof RecordTypeDSCP) {
            variable = new VariableDCL(getName(), getTypeDSCP().getName(), isConstant(), false);
        } else if (defaultValue.getResultType() instanceof ArrayTypeDSCP) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) defaultValue.getResultType();
            variable = new ArrayDCL(getName(), arrayTypeDSCP.getBaseType().getName(), arrayTypeDSCP.getDimensions(), isConstant(), false);
        } else {
            throw new RuntimeException("Can not detect type for auto variable");
        }
        variable.generateCode(currentClass, currentMethod, cv, mv, null, null);
        new DirectAssignment(new SimpleVariable(getName()), defaultValue).generateCode(currentClass, currentMethod, cv, mv, null, null);
    }
}
