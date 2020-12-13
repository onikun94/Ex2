package lang.c.parse;

import java.io.PrintStream;

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

		CTokenizer ct = pcx.getTokenizer();
		/*primaryのparse*/
		primary = new Primary(pcx);
		primary.parse(pcx);
		System.out.println("Primaryの解析終了");


		CToken tk = ct.getCurrentToken(pcx);
		System.out.println("StatementAssignのトークンの綴りは"+tk.getText());

		//System.out.println("tkは"+tk.getType());
		if(tk.getType() != CToken.TK_ASSIGN) {
			pcx.fatalError(tk.toExplainString() + "primaryの後には=がきます");
		}

		ct.getNextToken(pcx);

		/*expressionのparse*/
		expression = new Expression(pcx);
		expression.parse(pcx);
		System.out.println("Expressionの解析終了");

		tk = ct.getCurrentToken(pcx);//ここでエラー

		if(tk.getType() != CToken.TK_SEMI) {
			 pcx.fatalError(";がありません");
		}

		ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementAssignのsemanticCheck実行");
		if (primary != null) {
            primary.semanticCheck(pcx);
        }
        if (expression != null) {
            expression.semanticCheck(pcx);
        }
        if(primary.getCType().getType() != expression.getCType().getType()) {
        	pcx.fatalError(String.format("左辺の型[%s]と右辺の型[%s]が一致しません\n",
        			primary.getCType().toString(), expression.getCType().toString()));
        }

        if(primary.isConstant()) {
        	pcx.fatalError("定数には値を代入できません");
        }


	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementAssignのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		 o.println(";;; StatementAssign starts");
	        if (primary != null) {
	            primary.codeGen(pcx);
	        }
	        if (expression != null) {
	            expression.codeGen(pcx);
	        }
	        o.println("\tMOV\t-(R6), R1\t; StatementAssign: Expressionの値を取り出す");
	        o.println("\tMOV\t-(R6), R0\t; StatementAssign: 変数のアドレスを取り出す");
	        o.println("\tMOV\t   R1, (R0)\t; StatementAssign: 変数に値を代入する");
	        o.println(";;; StatementAssign completes");

	}
}
