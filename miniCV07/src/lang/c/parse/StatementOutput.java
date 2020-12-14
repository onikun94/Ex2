package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementOutput extends CParseRule {
	// term ::= factor
	private CParseRule expression;

	public StatementOutput(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_OUTPUT;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementOutputのparse実行");

		CTokenizer ct = pcx.getTokenizer();
		ct.getNextToken(pcx);

		expression = new Expression(pcx);
		expression.parse(pcx);
		System.out.println("Expressionの解析終了");

		CToken tk = ct.getCurrentToken(pcx);

		if(tk.getType() != CToken.TK_SEMI) {
			 pcx.fatalError(";がありません");
		}

		ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementOutputのsemanticCheck実行");
		if (expression != null) {
            expression.semanticCheck(pcx);
        }
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementOutputのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		 o.println(";;; StatementOutput starts");
	        if (expression != null) {
	            expression.codeGen(pcx);
	        }
            o.println("\tMOV\t#0xFFE0, R3\t; StatementOutput:");
            o.println("\tMOV\t-(R6),(R3)\t; StatementOutput: Expressionの値の書き込み");
	        o.println(";;; StatementOutput completes");

	}
}
