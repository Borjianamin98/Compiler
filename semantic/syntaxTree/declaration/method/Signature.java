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
}
