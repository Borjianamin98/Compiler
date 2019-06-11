package semantic.syntaxTree.block;

import semantic.syntaxTree.BlockCode;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private List<BlockCode> blockCodes;

    public Block() {
        this.blockCodes = new ArrayList<>();
    }
//
//    public Block(List<BlockCode> blockCodes) {
//        this.blockCodes = blockCodes;
//    }

    public void addBlockCode(BlockCode blockCode) {
        blockCodes.add(blockCode);
    }

    public List<BlockCode> getBlockCodes() {
        return blockCodes;
    }
}
