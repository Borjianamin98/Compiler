package semantic.syntaxTree.declaration.method;

import semantic.symbolTable.Utility;
import semantic.syntaxTree.declaration.Parameter;

public class Argument extends Parameter {

    public Argument(String name, String baseType, int dimensions) {
        super(name, baseType, dimensions);
    }

    @Override
    public String toString() {
        return getType().getConventionalName();
    }

    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        represent.append(Utility.getConvectionalRepresent(baseType));
        for (int i = 0; i < dimensions; i++) {
            represent.append("[]");
        }
        represent.append(" ").append(getName());
        return represent.toString();
    }
}
