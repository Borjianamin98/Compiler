package semantic.syntaxTree.declaration.method;

import semantic.syntaxTree.declaration.Parameter;

public class Argument extends Parameter {

    public Argument(String name, String baseType, int dimensions) {
        super(name, baseType, dimensions);
    }

    @Override
    public String toString() {
        return getType().getConventionalName();
    }


}
