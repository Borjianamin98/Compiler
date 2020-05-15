package lexical;

import syntax.ParserSym;

public enum Token {
    // Reversed Key
    _auto(ParserSym.AUTO),
    _double(ParserSym.DOUBLE), _float(ParserSym.FLOAT),
    _int(ParserSym.INT), _long(ParserSym.LONG),
    _string(ParserSym.STRING), _char(ParserSym.CHAR),
    _bool(ParserSym.BOOL),
    _const(ParserSym.CONST),
    _void(ParserSym.VOID),
    _record(ParserSym.RECORD),
    _function(ParserSym.FUNCTION),
    _repeat(ParserSym.REPEAT), _until(ParserSym.UNTIL),
    _for(ParserSym.FOR), _foreach(ParserSym.FOREACH), _in(ParserSym.IN),
    _if(ParserSym.IF), _switch(ParserSym.SWITCH), _else(ParserSym.ELSE), _case(ParserSym.CASE), _default(ParserSym.DEFAULT),
    _of(ParserSym.OF), _begin(ParserSym.BEGIN), _end(ParserSym.END),
    _sizeof(ParserSym.SIZEOF),
    _continue(ParserSym.CONTINUE), _break(ParserSym.BREAK), _return(ParserSym.RETURN),
    _true(ParserSym.BOOL_CONST, "true"), _false(ParserSym.BOOL_CONST, "false"),
    _println(ParserSym.PRINTLN, "println"), _input(ParserSym.INPUT, "input"), _len(ParserSym.LEN, "len"),
    _start(ParserSym.START, "start"),
    _new(ParserSym.NEW, "new"),

    // Identifier
    _id(ParserSym.IDENTIFIER, "identifier"),

    // Integer & Long & Double & Float & String & Character constant value
    _ic(ParserSym.INT_CONST, "int_const"),
    _lc(ParserSym.LONG_CONST, "long_const"),
    _dc(ParserSym.DOUBLE_CONST, "double_const"),
    _fc(ParserSym.FLOAT_CONST, "float_const"),
    _sc(ParserSym.STRING_CONST, "string_const"),
    _cc(ParserSym.CHAR_CONST, "char_const"),

    // Operators
    _equal(ParserSym.EQUAL, "=="), _notequal(ParserSym.NOT_EQUAL, "!="),
    _greater(ParserSym.GREATER, ">"), _greaterEqual(ParserSym.GREATER_EQUAL, ">="),
    _less(ParserSym.LESS, "<"), _lessEqual(ParserSym.LESS_EQUAL, "<="),
    _directAssign(ParserSym.DIRECT_ASSIGN, "="),
    _sumAssign(ParserSym.SUM_ASSIGN, "+="), _diffAssign(ParserSym.DIFF_ASSIGN, "-="),
    _multAssign(ParserSym.MULT_ASSIGN, "*="), _divideAssign(ParserSym.DIVIDE_ASSIGN, "/="),
    _modAssign(ParserSym.MOD_ASSIGN, "%="),
    _plus(ParserSym.PLUS, "+"), _minus(ParserSym.MINUS, "-"),
    _multiply(ParserSym.MULTIPLY, "*"), _divide(ParserSym.DIVIDE, "/"), _mod(ParserSym.MOD, "%"),
    _logicalOr(ParserSym.OR, "or"), _logicalAnd(ParserSym.AND, "and"), _negate(ParserSym.NOT, "not"),
    _or(ParserSym.BITWISE_OR, "|"), _and(ParserSym.BITWISE_AND, "&"), _xor(ParserSym.BITWISE_XOR, "^"), _not(ParserSym.BITWISE_NOT, "~"),
    _plusplus(ParserSym.PLUSPLUS, "++"), _minusminus(ParserSym.MINUSMINUS, "--"),

    // special
    _leftParen(ParserSym.LPAREN, "("), _rightParen(ParserSym.RPAREN, ")"),
    _leftBracket(ParserSym.LBRACKET, "["), _rightBracket(ParserSym.RBRACKET, "]"), _leftRightBracket(ParserSym.LRBRACKET, "[]"),
    _semicolon(ParserSym.SEMICOLON, ";"), _comma(ParserSym.COMMA, ","),
    _colon(ParserSym.COLON, ":"),
    _dot(ParserSym.DOT, ".");

    private int type;
    private String representation;

    Token(int type) {
        this.type = type;
        this.representation = "[NO REPRESENTATION]";
    }

    Token(int type, String representation) {
        this.type = type;
        this.representation = representation;
    }

    static Token getWithRepresentation(String representation) {
        for (Token value : Token.values()) {
            if (value.representation.equals(representation))
                return value;
        }
        throw new IllegalArgumentException("There is no type: " + representation);
    }

    public static Token getWithSym(int ParserSym) {
        for (Token value : Token.values()) {
            if (value.type == ParserSym)
                return value;
        }
        throw new IllegalArgumentException("There is no type: " + ParserSym);
    }

    public int getSym() {
        return type;
    }

    public String getName() {
        return name().substring(1);
    }

    @Override
    public String toString() {
        return "{name=" + name().substring(1) + ", type=" + type + '}';
    }
}
