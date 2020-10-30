package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;

public class TermDiv extends CParseRule {
	// term ::= factor
	private CParseRule factor;
	public TermDiv(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_DIV;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Termのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている
		factor = new Factor(pcx);
		factor.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのsemanticCheck実行");
		if (factor != null) {
			System.out.println("setCtype = " + factor.getCType());
			System.out.println("setConstant = " + factor.isConstant());
			factor.semanticCheck(pcx);
			this.setCType(factor.getCType());		// factor の型をそのままコピー
			this.setConstant(factor.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; termDiv starts");
		if (factor != null) {
		    factor.codeGen(pcx);
            o.println("\tJSR\tDIV\t; TermDiv:");
            o.println("\tSUB\t#2, R6\t; TermDiv:");
            o.println("\tMOV\tR0, (R6)+\t; TermDiv:");
		}
		o.println(";;; termDiv completes");
	}
}
