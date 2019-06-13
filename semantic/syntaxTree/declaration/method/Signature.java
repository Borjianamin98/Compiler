package semantic.syntaxTree.declaration.method;

import semantic.syntaxTree.block.Block;

import java.util.List;

public class Signature {
    private String name;
    private List<Argument> arguments;
    private Block body;

    public Signature(String name, List<Argument> arguments, Block body) {
        this.name = name;
        this.body = body;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public Block getBody() {
        return body;
    }

    public void setBody(Block body) {
        this.body = body;
    }

    public boolean hasBody() {
        return body != null;
    }

    @Override
    public String toString() {
        return name + "(" + arguments.toString().replace("[", "").replace("]", "") + ")";
    }

    /**
     * two signature are equals if they have same arguments in same order
     *
     * @param o other signature
     * @return true, if they have same arguments in same order
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return arguments.equals(signature.arguments);
    }

    public String getCodeRepresentation() {
        StringBuilder represent = new StringBuilder();
        represent.append(name).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            Argument argument = arguments.get(i);
            represent.append(argument.getCodeRepresentation());
            if (i < arguments.size() - 1)
                represent.append(", ");
        }
        represent.append(")");
        return represent.toString();
    }
}
