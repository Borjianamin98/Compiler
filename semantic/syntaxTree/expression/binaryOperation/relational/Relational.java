package semantic.syntaxTree.expression.binaryoperation.relational;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryoperation.BinaryOperation;
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
