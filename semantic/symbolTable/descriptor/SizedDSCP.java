package semantic.symbolTable.descriptor;

public class SizedDSCP implements DSCP {
    private int type;
    private int size;

    public SizedDSCP(int type, int size) {
        this.type = type;
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

}
