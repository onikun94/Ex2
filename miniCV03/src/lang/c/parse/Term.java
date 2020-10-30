package lang.c.parse;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Term extends CParseRule {
	// term ::= factor
	private CParseRule termMult;
	private CParseRule termDiv;

	public Term(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Factor.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Termのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている
		//factor = new Factor(pcx);
		//factor.parse(pcx);
		System.out.println("Factorのparse実行");
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);

		if(tk.getType() == CToken.TK_MUL) {
			termMult = new TermMult(pcx);
			termMult.parse(pcx);
		}else if(tk.getType() == CToken.TK_DIV) {
			System.out.println("FactorAmpに移動しろ");
			termDiv = new TermDiv(pcx);//追加
			termDiv.parse(pcx);//追加
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのsemanticCheck実行");
		if (termMult != null) {
			termMult.semanticCheck(pcx);
			this.setCType(termMult.getCType());
			this.setConstant(termMult.isConstant());
		}else if(termDiv != null) {
			termDiv.semanticCheck(pcx);
			this.setCType(termDiv.getCType());
			this.setConstant(termDiv.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {

	}
}
