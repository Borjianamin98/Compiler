package semantic;

import semantic.symbolTable.descriptor.TypeDSCP;

public class Constants {
    private Constants() {
    }

    public static final TypeDSCP INTEGER_DSCP = new TypeDSCP("int", Constants.INTEGER_CODE, Constants.INTEGER_SIZE, true);
    public static final TypeDSCP BOOLEAN_DSCP = new TypeDSCP("bool", Constants.INTEGER_CODE, Constants.INTEGER_SIZE, true);
    public static final TypeDSCP LONG_DSCP = new TypeDSCP("long", Constants.LONG_CODE, Constants.LONG_SIZE, true);
    public static final TypeDSCP FLOAT_DSCP = new TypeDSCP("float", Constants.FLOAT_CODE, Constants.FLOAT_SIZE, true);
    public static final TypeDSCP DOUBLE_DSCP = new TypeDSCP("double", Constants.DOUBLE_CODE, Constants.DOUBLE_SIZE, true);
    public static final TypeDSCP CHAR_DSCP = new TypeDSCP("char", Constants.CHAR_CODE, Constants.CHAR_SIZE, true);
    public static final TypeDSCP STRING_DSCP = new TypeDSCP("string", Constants.STRING_CODE, Constants.STRING_SIZE, true);

    public static final int VOID_CODE = -1;
    public static final int INTEGER_CODE = 1;
    public static final int LONG_CODE = 2;
    public static final int FLOAT_CODE = 3;
    public static final int DOUBLE_CODE = 4;
    public static final int CHAR_CODE = 5;
    public static final int STRING_CODE = 6;
    public static final int BOOLEAN_CODE = 7;

    public static final int INTEGER_SIZE = 1;
    public static final int LONG_SIZE = 2;
    public static final int FLOAT_SIZE = 1;
    public static final int DOUBLE_SIZE = 2;
    public static final int CHAR_SIZE = 1;
    public static final int STRING_SIZE = -1;
    public static final int BOOLEAN_SIZE = 1;
    public static final int REFERENCE_SIZE = 1;
}
