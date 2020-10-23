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
        System.out.println("tk.getType()="+tk.getType());
        System.out.println("TK_NUM="+CToken.TK_NUM);

        System.out.println("FactorAmpのparse");

        numType.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		final int s[] = {
//				T_int
				CType.T_pint
		};
		if (numType != null ) {
			numType.semanticCheck(pcx);

			int tp = numType.getCType().getType();
			int nt = s[tp];						// 規則による型計算
			this.setCType(CType.getCType(nt));
			this.setConstant(numType.isConstant());	// -の左右両方が定数のときだけ定数
		}

	}


	public void codeGen(CParseContext pcx) throws FatalErrorException {
		numType.codeGen(pcx);
	}
}
