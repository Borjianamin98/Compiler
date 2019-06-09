package semantic.syntaxTree.declaration.method;

import semantic.syntaxTree.block.Block;

import java.util.ArrayList;
import java.util.List;

public class StartMethodDCL extends MethodDCL {
    private static List<Argument> arguments;
    static {
        arguments = new ArrayList<>();
        arguments.add(new Argument("args", "string", 1));
    }

    public StartMethodDCL(String owner, Block body) {
        super(owner, "main", arguments, body, true);
    }
}
