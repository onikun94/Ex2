package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementInput extends CParseRule {
	// term ::= factor
	private CParseRule primary;

	public StatementInput(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_INPUT;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementInputのparse実行");

		CTokenizer ct = pcx.getTokenizer();
		ct.getNextToken(pcx);
		/*primaryのparse*/
		primary = new Primary(pcx);
		primary.parse(pcx);
		System.out.println("Primaryの解析終了");

		CToken tk = ct.getCurrentToken(pcx);

		if(tk.getType() != CToken.TK_SEMI) {
			 pcx.fatalError(";がないか不正な値をinputしています");
		}

		ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementInputのsemanticCheck実行");
		if (primary != null) {
            primary.semanticCheck(pcx);

            if(primary.isConstant()) {
            	pcx.fatalError("定数には値を代入できません");
            }
        }


	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementInputのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		 o.println(";;; StatementInput starts");
	        if (primary != null) {
	            primary.codeGen(pcx);
	        }

	        o.println("\tMOV\t-(R6), R0\t; StatementInput: 変数のアドレスを取り出す");
            o.println("\tMOV\t#0xFFE0, R3\t; StatementInput:");
            o.println("\tMOV\t(R3), (R0)\t; StatementInput: Primaryを変数に書き込む");
	        o.println(";;; StatementInput completes");

	}
}
