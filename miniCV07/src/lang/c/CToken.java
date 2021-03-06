package lang.c;

import lang.SimpleToken;

public class CToken extends SimpleToken {
	public static final int TK_PLUS			= 2;                // +
	public static final int TK_MINUS		    = 3;                // -
	public static final int TK_COMMENT		= 4;                // comment
	public static final int TK_AMP		    = 5;                // &
    public static final int TK_DIV           = 6;                // /
    public static final int TK_MUL           = 7;                // *
    public static final int TK_LPAR          = 8;                // (
    public static final int TK_RPAR          = 9;                // )
    public static final int TK_LBRA          = 10;               // [
    public static final int TK_RBRA          = 11;               // ]
    public static final int TK_IDENT         = 12;               // variable
    public static final int TK_ASSIGN        = 13;               // assign =
    public static final int TK_SEMI          = 14;               // semi ;
    public static final int TK_LT            = 15;               // <
    public static final int TK_LE            = 16;               // <=
    public static final int TK_GT            = 17;               // >
    public static final int TK_GE            = 18;               // >=
    public static final int TK_EQ            = 19;               // ==
    public static final int TK_NE            = 20;                // !=
    public static final int TK_TRUE          = 21;                // true
    public static final int TK_FALSE         = 22;                // false
    public static final int TK_LCUR          = 23;                // {
    public static final int TK_RCUR          = 24;                // }
    public static final int TK_IF            = 25;                // if
    public static final int TK_ELSE          = 26;                // else
    public static final int TK_WHILE         = 27;                // while
    public static final int TK_INPUT         = 28;                // input
    public static final int TK_OUTPUT        = 29;                // output

	public CToken(int type, int lineNo, int colNo, String s) {
		super(type, lineNo, colNo, s);
	}
}
