package lang.c.parse;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementAssign extends CParseRule {
	// term ::= factor
	private CParseRule primary;
	private CParseRule expression;

	public StatementAssign(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Primary.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementAssignのparse実行");

		/*primaryのparse*/
		primary = new Primary(pcx);
		primary.parse(pcx);

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);

		if(tk.getType() != CToken.TK_ASSIGN) {
			pcx.fatalError(tk.toExplainString() + "primaryの後には=がきます");
		}

		ct.getNextToken(pcx);

		/*expressionのparse*/
		expression = new Expression(pcx);
		expression.parse(pcx);
		tk = ct.getCurrentToken(pcx);

		if(tk.getType() != CToken.TK_SEMI) {
			 pcx.fatalError(tk.toExplainString() + ";がありません");
		}

		ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementAssignのsemanticCheck実行");

	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementAssignのcodeGen実行");

	}
}
