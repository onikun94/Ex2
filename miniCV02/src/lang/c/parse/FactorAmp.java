package lang.c.parse;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class FactorAmp extends CParseRule {
	// term ::= factor
	private CToken op;
	private CParseRule numType;
	public FactorAmp(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Factor.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		//factor = new Factor(pcx);
		//factor.parse(pcx);
		   // ここにやってくるときは、必ずisFirst()が満たされている
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        // &の次の字句を読む
        CToken tk = ct.getNextToken(pcx);


	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (numType != null) {
			numType.semanticCheck(pcx);
			final int type = numType.getCType().getType();
			int res = 0;
			if(type == CType.T_int) {
				res = CType.T_pint;
			}else {
				 pcx.fatalError(type + "不正な型です");
			}
			this.setCType(CType.getCType(res));
			this.setConstant(numType.isConstant());
		}
		System.out.print("semanticCheckは動いています");
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		numType.codeGen(pcx);
	}
}
