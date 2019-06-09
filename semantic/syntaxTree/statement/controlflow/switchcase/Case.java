package semantic.syntaxTree.statement.controlflow.switchcase;

import semantic.syntaxTree.block.Block;

public class Case {
    private int number;
    private Block block;

    public Case(int number, Block block) {
        this.number = number;
        this.block = block;
    }

    public int getNumber() {
        return number;
    }

    public Block getBlock() {
        return block;
    }
}
