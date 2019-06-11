package lexical;

import syntax.ParserSym;

public enum Token {
    // Reversed Key
    _auto(ParserSym.AUTO),
    _double(ParserSym.DOUBLE), _float(ParserSym.FLOAT),
    _int(ParserSym.INT), _long(ParserSym.LONG),
//    _short(SubType.Reserved)
    _string(ParserSym.STRING), _char(ParserSym.CHAR),
    _bool(ParserSym.BOOL),
    _const(ParserSym.CONST),
//    _unsigned(SubType.Reserved), _signed(SubType.Reserved),
    _void(ParserSym.VOID),
    _record(ParserSym.RECORD),
    _function(ParserSym.FUNCTION),
//    _enum(SubType.Reserved), _class(SubType.Reserved), _union(SubType.Reserved),
//    _public(SubType.Reserved), _protected(SubType.Reserved), _private(SubType.Reserved), _virtual(SubType.Reserved),
    _repeat(ParserSym.REPEAT), _until(ParserSym.UNTIL),
    _for(ParserSym.FOR), _foreach(ParserSym.FOREACH), _in(ParserSym.IN),
    _if(ParserSym.IF), _switch(ParserSym.SWITCH), _else(ParserSym.ELSE), _case(ParserSym.CASE), _default(ParserSym.DEFAULT),
    _of(ParserSym.OF), _begin(ParserSym.BEGIN), _end(ParserSym.END),
//    _register(SubType.Reserved), _static(SubType.Reserved), _typedef(SubType.Reserved),
    _extern(ParserSym.EXTERN),
//    _goto(SubType.Reserved), _volatile(SubType.Reserved),
    _sizeof(ParserSym.SIZEOF),
    _continue(ParserSym.CONTINUE), _break(ParserSym.BREAK), _return(ParserSym.RETURN),
//    _NULL(SubType.Reserved), _nullptr(SubType.Reserved),
    _true(ParserSym.BOOL_CONST, "true"), _false(ParserSym.BOOL_CONST, "false"),
//    _new(SubType.Reserved), _delete(SubType.Reserved),
//    _using(SubType.Reserved), _namespace(SubType.Reserved),


    // Identifier
    _id(ParserSym.IDENTIFIER, "identifier"),

    // Integer & Long & Real & String & Character constant value
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
//    _leftShift(SubType.Special, "<<"), _rightShift(SubType.Special, ">>"),
    _plusplus(ParserSym.PLUSPLUS, "++"), _minusminus(ParserSym.MINUSMINUS, "--"),

    // special
    _leftParen(ParserSym.LPAREN, "("), _rightParen(ParserSym.RPAREN, ")"),
    //    _openCurlyBracket(SubType.Special, "{"), _closeCurlyBracket(SubType.Special, "}"),
    _leftBracket(ParserSym.LBRACKET, "["), _rightBracket(ParserSym.RBRACKET, "]"), _leftRightBracket(ParserSym.LRBRACKET, "[]"),
    _semicolon(ParserSym.SEMICOLON, ";"), _comma(ParserSym.COMMA, ","),
//    _twocolon(SubType.Special, "::"),
    _colon(ParserSym.COLON, ":"),
    _dot(ParserSym.DOT, ".");
//    _arrow(SubType.Special, "->"), _question(SubType.Special, "?"),

    // Tabs, Spaces, ...
//    _ignore(SubType.Ignore);

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
