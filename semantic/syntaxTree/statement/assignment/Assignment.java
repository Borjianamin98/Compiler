package semantic.syntaxTree.statement.assignment;

import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.statement.Statement;

public abstract class Assignment extends Statement {
    private Variable variable;
    private Expression value;
    private String sign;

    public Assignment(String sign, Variable variable, Expression value) {
        this.sign = sign;
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getValue() {
        return value;
    }

    public String getCodeRepresentation() {
        return variable.getCodeRepresentation() + " " + sign + " " + value.getCodeRepresentation();
    }
}
