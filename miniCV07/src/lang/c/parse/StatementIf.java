package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementIf extends CParseRule {
	// term ::= factor
	private CParseRule condition;
	private CParseRule statement;
	private CParseRule statementElse;
	private int seq;

	public StatementIf(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_IF;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementIFのparse実行");

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getNextToken(pcx);

		if(tk.getType() != CToken.TK_LPAR) {
			pcx.fatalError(tk.toExplainString() + "ifの後ろには（がきます");
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
		System.out.println("現在のトークン");

		statement = new Statement(pcx);
		statement.parse(pcx);

		tk = ct.getCurrentToken(pcx);

		//elseの確認
		if(tk.getType() == CToken.TK_ELSE) {
			ct.getNextToken(pcx);
			if(!Statement.isFirst(tk)) {
				 pcx.fatalError(tk.toExplainString() + "elseには文がこなければなりません");
			}

			statementElse = new Statement(pcx);
			statementElse.parse(pcx);

		}

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementWhileのsemanticCheck実行");
		if (condition != null) {
            condition.semanticCheck(pcx);
        }

		if (statement != null) {
            statement.semanticCheck(pcx);
        }

		if (statementElse != null) {
            statement.semanticCheck(pcx);
        }
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementIfのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		seq = pcx.getSeqId();
		 o.println(";;; StatementIf starts");
	        if (condition != null) {
	            condition.codeGen(pcx);
	        }

	        o.println("\tMOV\t-(R6), R0\t;StatementIf: スタックトップからconditionの結果を持ってくる");
	        o.println("BRZ ifEnd" + seq + "\t;;; StatementIf:");

	        if (statement != null) {
	            statement.codeGen(pcx);
	        }

	        if(statementElse != null) {
	            o.println("\tJMP elseEnd" + seq + "\t;;; StatementIF:");
	            o.println("ifEnd" + seq + ": \t\t;StatementIF: ラベル名を生成する");

	        	statementElse.codeGen(pcx);

	        	o.println("elseEnd" + seq + ": \t\t;StatementIF: ラベル名を生成する");

	        }else {
	        	o.println("ifEnd" + seq + ": \t\t;StatementIF: ラベル名を生成する");
	        }

	        o.println(";;; StatementIf Completes");

	}
}
