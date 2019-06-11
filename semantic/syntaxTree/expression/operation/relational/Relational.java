package semantic.syntaxTree.expression.operation.relational;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Relational extends BinaryOperation {

    public Relational(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

}
