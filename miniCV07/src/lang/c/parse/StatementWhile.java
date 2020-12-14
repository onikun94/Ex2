package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementWhile extends CParseRule {
	// term ::= factor
	private CParseRule condition;
	private CParseRule statement;
	private int seq;

	public StatementWhile(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_WHILE;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementWhileのparse実行");

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getNextToken(pcx);

		if(tk.getType() != CToken.TK_LPAR) {
			pcx.fatalError(tk.toExplainString() + "whileのあとには（がきます");
		}

		ct.getNextToken(pcx);

		condition = new Condition(pcx);
		condition.parse(pcx);
		System.out.println("condition解析の終了");

		tk = ct.getCurrentToken(pcx);

		if(tk.getType() != CToken.TK_RPAR) {
			 pcx.fatalError("()が閉じていません");
		}

		ct.getNextToken(pcx);

		statement = new Statement(pcx);
		statement.parse(pcx);


	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementWhileのsemanticCheck実行");
		if (condition != null) {
            condition.semanticCheck(pcx);
        }

		if (statement != null) {
            statement.semanticCheck(pcx);
        }
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementWhileのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		seq = pcx.getSeqId();
		 o.println(";;; StatementWhile starts");
		 o.println("while" + seq + ":\t;StatementWhile: ラベル名を生成する");
	        if (condition != null) {
	            condition.codeGen(pcx);
	        }

	        o.println("\tMOV\t-(R6), R0\t;StatementWhile: スタックトップからconditionの結果を持ってくる");
	        o.println("BRZ whileEnd" + seq + "\t;;; StatementWhile:");

	        if (statement != null) {
	            statement.codeGen(pcx);
	        }

	        o.println("\tJMP while" + seq + ":\t;StatementWhile:");
	        o.println("whileEnd" + seq + ":\t;StatementWhile: ラベル名を生成する");
	        o.println(";;; StatementWhile Completes");

	}
}
