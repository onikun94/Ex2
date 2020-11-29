package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;

public class Statement extends CParseRule {
	// term ::= factor
	private CParseRule statementAssign;
	public Statement(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		 return StatementAssign.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Statementのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている
		statementAssign = new StatementAssign(pcx);
		statementAssign.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのsemanticCheck実行");
		 if (statementAssign != null) {
	            statementAssign.semanticCheck(pcx);
	        }

	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; Statement starts");
        if (statementAssign != null) {
            statementAssign.codeGen(pcx);
        }
        o.println(";;; Statement completes");
    }
}
