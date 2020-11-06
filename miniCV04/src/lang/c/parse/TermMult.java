package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CType;

public class TermMult extends CParseRule {
	// term ::= factor
	private CParseRule factor;
	public TermMult(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_MUL;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Termのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている
        //CTokenizer ct = pcx.getTokenizer();
        //CToken tk = ct.getCurrentToken(pcx);
		factor = new Factor(pcx);
		factor.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのsemanticCheck実行");
		if (factor != null) {
			factor.semanticCheck(pcx);
			int type = factor.getCType().getType();
			if(type == CType.T_pint) {
				 pcx.fatalError("乗算にポインタは使えません");
			}

			this.setCType(factor.getCType());		// factor の型をそのままコピー
			this.setConstant(factor.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermMultのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; termMult starts");
		if (factor != null) {
            factor.codeGen(pcx);
            o.println("\tJSR\tMUL\t; TermMul:");
            o.println("\tSUB\t#2, R6\t; TermMul:");
            o.println("\tMOV\tR0, (R6)+\t; TermMul:");
        }
        o.println(";;; termMult completes");
	}
}
