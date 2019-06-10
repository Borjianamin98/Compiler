package semantic.syntaxTree.expression.binaryoperation.logical;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.binaryoperation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Logical extends BinaryOperation {

    public Logical(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }


}
