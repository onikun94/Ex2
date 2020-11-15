package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class FactorAmp extends CParseRule {
	// term ::= factor
	//private CToken op;
	private CParseRule numPrime;
	public static boolean isAddress = false;

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
        	numPrime = new Number(pcx);
        	numPrime.parse(pcx);
        } else {
        	numPrime = new Primary(pcx);
        	isAddress = true;
        	numPrime.parse(pcx);
        }

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorAmpのsemanticCheck実行");
		final int s[] = {
        //		T_err			T_int              T_pint            T_int_arr
				CType.T_err,    CType.T_pint,      CType.T_err,      CType.T_pint_arr,
		};

		if (numPrime != null ) {
	            if (numPrime instanceof Primary) {
	            	System.out.println("FactorAmpの子節点にprimaryがつながっているとき");
	            	System.out.println(Primary.isMultPrimary);
	                if (Primary.isMultPrimary) {
	                	System.out.println("primaryMultクラスのオブジェクトが来ていないか確認");
	                    pcx.fatalError("&の後ろに*は付けられません");
	                }
	            }
			numPrime.semanticCheck(pcx);

			int tp = numPrime.getCType().getType();
			int nt = s[tp];						// 規則による型計算
			if (nt == CType.T_err) {
				pcx.fatalError(tp+ "適切な型ではありません");
			}
			this.setCType(CType.getCType(nt));
			this.setConstant(numPrime.isConstant());
		}

	}


	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorAmpのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; factorAmp starts");
		// factorAmp object will generate address
		numPrime.codeGen(pcx);
		o.println(";;; factorAmp completes");
	}
}
