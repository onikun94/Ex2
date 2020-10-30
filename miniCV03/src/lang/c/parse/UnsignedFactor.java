package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class UnsignedFactor extends CParseRule {
	// factor ::= number
	private CParseRule unsignedFactor;


	public UnsignedFactor(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
        return Number.isFirst(tk)|| FactorAmp.isFirst(tk)|| tk.getType() == CToken.TK_LPAR ;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("Factorのparse実行");
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		if(tk.getType() == CToken.TK_AMP) {
			unsignedFactor = new Number(pcx);
			unsignedFactor.parse(pcx);
		}else if(tk.getType() == CToken.TK_LPAR) {
			 tk = ct.getNextToken(pcx);

			 if(Expression.isFirst(tk)) {
				 unsignedFactor = new Expression(pcx);
	             unsignedFactor.parse(pcx);
	             tk = ct.getCurrentToken(pcx);
	                if (tk.getType() != CToken.TK_RPAR) {
	                    pcx.fatalError(tk.toExplainString() + "()が閉じていません.");
	                }
	                tk = ct.getNextToken(pcx);
	            } else {
	                pcx.fatalError(tk.toExplainString() + "TK_LPARの後ろはExpressionです");
	            }
			 }
		else if (tk.getType() == CToken.TK_NUM) {
            unsignedFactor = new Number(pcx);
            unsignedFactor.parse(pcx);
        }

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorのsemanticCheck実行");
		if (unsignedFactor != null) {
			unsignedFactor.semanticCheck(pcx);
			setCType(unsignedFactor.getCType());
			setConstant(unsignedFactor.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; unsignedFactor starts");
		if (unsignedFactor != null) { unsignedFactor.codeGen(pcx); }
		o.println(";;; unsignedFactor completes");
	}
}