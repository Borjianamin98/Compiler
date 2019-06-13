package semantic.syntaxTree.declaration.method;

import semantic.syntaxTree.block.Block;
import semantic.symbolTable.typeTree.TypeTree;

import java.util.ArrayList;
import java.util.List;

public class StartMethodDCL extends MethodDCL {
    private static Signature signature;

    static {
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument("args", TypeTree.STRING_NAME, 1));
        signature = new Signature("main", arguments, null);
    }

    public StartMethodDCL(String owner, Block body) {
        super(owner, signature,TypeTree.VOID_NAME, true);
        signature.setBody(body);
    }
}
