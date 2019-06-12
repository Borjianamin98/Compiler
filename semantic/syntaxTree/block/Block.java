package semantic.syntaxTree.block;

import semantic.syntaxTree.BlockCode;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private List<BlockCode> blockCodes;

    public Block() {
        this.blockCodes = new ArrayList<>();
    }

    public void addBlockCode(BlockCode blockCode) {
        blockCodes.add(blockCode);
    }

    public void addBlockCodes(List<BlockCode> codes) {
        if (codes == null)
            throw new IllegalArgumentException("Block code must be not null");
        this.blockCodes.addAll(codes);
    }

    public List<BlockCode> getBlockCodes() {
        return blockCodes;
    }
}
