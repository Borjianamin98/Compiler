package semantic.symbolTable.descriptor;

public abstract class DSCP {
    private String name;

    public DSCP(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
