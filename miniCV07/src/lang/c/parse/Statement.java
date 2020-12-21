package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Statement extends CParseRule {
	// term ::= factor
	private CParseRule statement;
	public Statement(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		 return StatementAssign.isFirst(tk)
				 || StatementIf.isFirst(tk)
				 ||StatementWhile.isFirst(tk)
				 || StatementInput.isFirst(tk)
				 || StatementOutput.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Statementのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);

		if(StatementAssign.isFirst(tk)) {
			statement = new StatementAssign(pcx);
		}else if(StatementIf.isFirst(tk)) {
			System.out.println(tk.toExplainString());
			statement = new StatementIf(pcx);
		}else if(StatementWhile.isFirst(tk)) {
			statement = new StatementWhile(pcx);
		}else if(StatementInput.isFirst(tk)) {
			statement = new StatementInput(pcx);
		}else if(StatementOutput.isFirst(tk)) {
			statement = new StatementOutput(pcx);
		}else {
			pcx.fatalError(tk.toExplainString() + "予期せぬトークンです");
		}
		statement.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementのsemanticCheck実行");
		 if (statement != null) {
	            statement.semanticCheck(pcx);
	        }

	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; Statement starts");
        if (statement != null) {
            statement.codeGen(pcx);
        }
        o.println(";;; Statement completes");
    }
}
