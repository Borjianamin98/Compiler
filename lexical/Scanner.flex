package lexical;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import syntax.ParserSym;

%%
%class LexicalAnalyzer
%unicode
%line
%column
%cup
%public
//%type Token

%{
    StringBuffer string = new StringBuffer();

    public LexicalAnalyzer(java.io.Reader in, ComplexSymbolFactory sf) {
        this(in);
        symbolFactory = sf;
    }

    ComplexSymbolFactory symbolFactory;

    private Symbol symbol(String name, int sym) {
        return symbolFactory.newSymbol(name + " " + ParserSym.terminalNames[sym], sym, new Location(yyline + 1, yycolumn + 1, yychar), new Location(yyline + 1, yycolumn + yylength(), yychar + yylength()));
    }

    private Symbol symbol(String name, int sym, Object val) {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right = new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name + " " + ParserSym.terminalNames[sym] + " " + val, sym, left, right, val);
    }

    private Symbol symbol(String name, int sym, Object val, int buflength) {
        Location left = new Location(yyline + 1, yycolumn + yylength() - buflength, yychar + yylength() - buflength);
        Location right = new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
        return symbolFactory.newSymbol(name + " " + ParserSym.terminalNames[sym] + " " + val, sym, left, right, val);
    }
%}

%eofval{
     return symbol("EOF", ParserSym.EOF);
%eofval}

LineTerminator = \r|\n|\r\n
//InputCharacter = [^\r\n ]
WhiteSpace = {LineTerminator} | [ \t\f]

EndOfLineComment = "##" [^\r\n]* \R?

// removed reserverd: "goto"|"enum"|"class"|"union"|"short"|"signed"|"typedef"|"volatile"|"register"|"unsigned"|"NULL"|"nullptr"|"delete"|"public"|"private"|"protected"|"virtual"|"using"|"namespace"
ReservedKeyword = "function"|"repeat"|"until"|"if"|"of"|"begin"|"end"|"for"|"foreach"|"in"|"int"|"bool"|"auto"|"char"|"long"|"else"|"void"|"case"|"while"|"const"|"float"|"extern"|"break"|"return"|"string"|"sizeof"|"double"|"static"|"switch"|"default"|"continue"|"record"|"new"|"println"
Identifier = [:jletter:] [:jletterdigit:]*
// removed operator: "<<"|">>"
Operator = "="|"!="|">"|">="|"<"|"<="|"="|"+="|"-="|"*="|"/="|"%="|"+"|"-"|"*"|"/"|"%"|"not"|"or"|"and"|"|"|"&"|"^"|"~"|"++"|"--"
Special = "("|")"|"{"|"}"|"["|"]"|"[]"|";"|","|"::"|":"|"."|"->"|"*"|"?"

integer = 0 | [-]?[1-9][0-9]* | [-]?0(x|X)[0-9A-Fa-f]+ | [-]?0[0-7]+
realNormal = [-]?{integer}?"."[0-9]+ | [-]?{integer}"."
realScientific = {realNormal}[eE]{integer} | {integer}[eE]{integer}
real = {realNormal} | {realScientific}
escapeChar = "\\'"|"\\\""|"\\\\"|"\\n"|"\\r"|"\\t"|"\\b"|"\\f"|"\\v"|"\\0"
character = "'"."'"
escapeCharacter = "'"{escapeChar}"'"

%xstate STRING , COMMENT

%%

<YYINITIAL> {

    {ReservedKeyword}           { return symbol("Reserved", Token.valueOf("_" + yytext()).getSym());}

    "true"                      { return symbol("Reserved", Token.valueOf("_" + yytext()).getSym(), 1);}

    "false"                     { return symbol("Reserved", Token.valueOf("_" + yytext()).getSym(), 0);}

    {Operator}                  { return symbol("Operator", Token.getWithRepresentation(yytext()).getSym()); }

    {Special}                   { return symbol("Special", Token.getWithRepresentation(yytext()).getSym()); }

    {Identifier}                { return symbol("Identifier", Token.getWithRepresentation("identifier").getSym(), yytext()); }

    /* literals */
    {integer}                   { return symbol("Integer", Token.getWithRepresentation("int_const").getSym(), Integer.parseInt(yytext())); }
    {integer}[lL]               { return symbol("Long", Token.getWithRepresentation("long_const").getSym(), Long.parseLong(yytext())); }
    {real}                      { return symbol("Double", Token.getWithRepresentation("double_const").getSym(), Double.valueOf(yytext())); }
    {real}[fF]                  { return symbol("Float", Token.getWithRepresentation("float_const").getSym(), Float.valueOf(yytext())); }
    {character}                 { return symbol("Character", Token.getWithRepresentation("char_const").getSym(), yytext().substring(1, 2).charAt(0)); }
    {escapeCharacter}           { return symbol("EscapeCharacter", Token.getWithRepresentation("char_const").getSym(), yytext().substring(1, yytext().length() - 1).charAt(0)); }
    \"                          { string.setLength(0); string.append(yytext()); yybegin(STRING); }

    /* comments */
    {EndOfLineComment}          { /* skip */ }

    /* multiple Line Comment */
    "/#"                        { yybegin(COMMENT); }

    /* whitespace */
    {WhiteSpace}+               { /* skip */ }

//    {InputCharacter}+           { return symbol(lexical.Token._ignore, yytext()); }
}

<COMMENT> {
    "#/"                        { yybegin(YYINITIAL);}

    \R+                         { /* skip */ }
    [^#]+                       { /* skip */ }
    "#"[^/]                     { /* skip */ }
}

<STRING> {
    [^\n\r\"\\]+                { string.append( yytext() ); }

    {escapeChar}               { string.append(yytext()); }

    "\""                        { yybegin(YYINITIAL);
                                    string.append(yytext());
                                    return symbol("String", Token.getWithRepresentation("string_const").getSym(), string.toString()); }
}

/* error fallback */
[^]                             { throw new Error("Illegal character <" + yytext() + ">"); }