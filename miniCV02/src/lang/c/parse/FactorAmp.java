package lang.c.parse;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class FactorAmp extends CParseRule {
	// term ::= factor
	//private CToken op;
	private CParseRule numType;

	public FactorAmp(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() ==  CToken.TK_AMP;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        //CToken fa = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);

        if(tk.getType() == CToken.TK_NUM) {
        	numType = new Number(pcx);
        }

        numType.parse(pcx);
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
