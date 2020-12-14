package lang.c;

import java.util.HashMap;

public class CTokenRule extends HashMap<String, Object> {
	private static final long serialVersionUID = 1139476411716798082L;

	public CTokenRule() {
//		put("int",		new Integer(CToken.TK_INT));
		 put("true", CToken.TK_TRUE);
	     put("false", CToken.TK_FALSE);
	     put("if", CToken.TK_IF);
	     put("else", CToken.TK_ELSE);
	     put("while", CToken.TK_WHILE);
	     put("input", CToken.TK_INPUT);
	     put("output", CToken.TK_OUTPUT);

	}
}
