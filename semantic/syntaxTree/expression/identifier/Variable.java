package semantic.syntaxTree.expression.identifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import semantic.symbolTable.descriptor.hastype.HasTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.program.ClassDCL;

public abstract class Variable extends Expression {
    /**
     * represent a final name of variable if apply array index or member field on variable
     */
    private String chainName;

    public Variable(String chainName) {
        this.chainName = chainName;
    }

    public abstract void assignValue(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Expression value);

    public abstract HasTypeDSCP getDSCP();

    @Override
    public TypeDSCP getResultType() {
        return getDSCP().getType();
    }

    public String getChainName() {
        return chainName;
    }
}
