package semantic.symbolTable;

import semantic.symbolTable.descriptor.type.SimpleTypeDSCP;

public class Constants {
    private Constants() {
    }

    public static final SimpleTypeDSCP INTEGER_DSCP = new SimpleTypeDSCP("int", Constants.INTEGER_SIZE);
    public static final SimpleTypeDSCP BOOLEAN_DSCP = new SimpleTypeDSCP("bool", Constants.INTEGER_SIZE);
    public static final SimpleTypeDSCP LONG_DSCP = new SimpleTypeDSCP("long", Constants.LONG_SIZE);
    public static final SimpleTypeDSCP FLOAT_DSCP = new SimpleTypeDSCP("float", Constants.FLOAT_SIZE);
    public static final SimpleTypeDSCP DOUBLE_DSCP = new SimpleTypeDSCP("double", Constants.DOUBLE_SIZE);
    public static final SimpleTypeDSCP CHAR_DSCP = new SimpleTypeDSCP("char", Constants.CHAR_SIZE);
    public static final SimpleTypeDSCP STRING_DSCP = new SimpleTypeDSCP("string", Constants.STRING_SIZE);
    public static final SimpleTypeDSCP VOID_DSCP = new SimpleTypeDSCP("void", 0);

    public static final int INTEGER_SIZE = 1;
    public static final int LONG_SIZE = 2;
    public static final int FLOAT_SIZE = 1;
    public static final int DOUBLE_SIZE = 2;
    public static final int CHAR_SIZE = 1;
    public static final int STRING_SIZE = -1;
    public static final int BOOLEAN_SIZE = 1;
    public static final int REFERENCE_SIZE = 1;
}
