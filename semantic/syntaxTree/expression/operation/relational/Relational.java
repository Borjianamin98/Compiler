package semantic.syntaxTree.expression.operation.relational;

import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.operation.BinaryOperation;
import semantic.typeTree.TypeTree;

public abstract class Relational extends BinaryOperation {
    private String relationalSign;

    public Relational(String relationalSign, Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
        this.relationalSign = relationalSign;
    }

    @Override
    public TypeDSCP getResultType() {
        return TypeTree.INTEGER_DSCP;
    }

    @Override
    public String getCodeRepresentation() {
        return "(" + getFirstOperand().getCodeRepresentation() + " " + relationalSign + " " +
                getSecondOperand().getCodeRepresentation() + ")";
    }
}
