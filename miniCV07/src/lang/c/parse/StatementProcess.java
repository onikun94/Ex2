package lang.c.parse;

import java.util.ArrayList;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class StatementProcess extends CParseRule {
	private CParseRule statement;
	ArrayList<CParseRule> statementList = new ArrayList<>();

	public StatementProcess(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_LCUR;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementProcessのparse実行");

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getNextToken(pcx);
		while (Statement.isFirst(tk)) {
			statement = new Statement(pcx);
			statement.parse(pcx);
			statementList.add(statement);
			tk = ct.getCurrentToken(pcx);

		}
		if(tk.getType() != CToken.TK_RCUR) {
			pcx.fatalError(tk.toExplainString() + "{}が閉じていません");
		}
		ct.getNextToken(pcx);

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementWhileのsemanticCheck実行");
		statementList.forEach(statement -> {
            try {
                statement.semanticCheck(pcx);
            } catch (FatalErrorException e) {
                e.printStackTrace();
            }
        });
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("StatementIfのcodeGen実行");
		statementList.forEach(statement -> {
            try {
                statement.codeGen(pcx);
            } catch (FatalErrorException e) {
                e.printStackTrace();
            }
        });

	}
}
