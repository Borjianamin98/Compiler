package semantic.syntaxTree.statement.assignment;

import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.identifier.Variable;

public abstract class Assignment extends Statement {
    private Variable variable;
    private Expression value;

    public Assignment(Variable variable, Expression value) {
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getValue() {
        return value;
    }
}
