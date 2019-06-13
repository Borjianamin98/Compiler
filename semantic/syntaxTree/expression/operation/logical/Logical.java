package semantic.syntaxTree.expression.operation.logical;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.symbolTable.typeTree.TypeTree;

public abstract class Logical extends BinaryOperation {
    private String logicalSign;

    public Logical(String logicalSign, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.logicalSign = logicalSign;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public String getCodeRepresentation() {
        return "(" + getFirstOperand().getCodeRepresentation() + " " + logicalSign + " " +
                getSecondOperand().getCodeRepresentation() + ")";
    }
}
