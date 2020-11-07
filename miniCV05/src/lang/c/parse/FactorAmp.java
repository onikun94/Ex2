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
		System.out.println("FactorAmpのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        //CToken tk = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if(tk.getType() == CToken.TK_NUM) {
        	numType = new Number(pcx);
        	numType.parse(pcx);
        } else {
        	numType = new Primary(pcx);
        	numType.parse(pcx);
        }

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorAmpのsemanticCheck実行");
		final int s[] = {
        //		T_err			T_int              T_pint            T_int_arr
				CType.T_err,    CType.T_pint,      CType.T_err,      CType.T_pint_arr,
		};

		if (numType != null ) {
	            if (numType instanceof Primary) {
	                if (((Primary) numType).isMultPrimary) {
	                    pcx.fatalError("&の後ろに*は付けられません");
	                }
	            }
			numType.semanticCheck(pcx);

			int tp = numType.getCType().getType();
			int nt = s[tp];						// 規則による型計算
			if (nt == CType.T_err) {
				pcx.fatalError(tp+ "適切ではありません");
			}
			this.setCType(CType.getCType(nt));
			this.setConstant(numType.isConstant());
		}

	}


	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorAmpのcodeGen実行");
		numType.codeGen(pcx);
	}
}
