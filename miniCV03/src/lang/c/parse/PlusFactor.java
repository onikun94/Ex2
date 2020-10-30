package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class PlusFactor extends CParseRule {
	// factor ::= number
	private CParseRule unsignedFactor;


	public PlusFactor(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_PLUS;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("Factorのparse実行");
		CTokenizer ct = pcx.getTokenizer();
		CToken op = ct.getCurrentToken(pcx);
		CToken tk = ct.getNextToken(pcx);

		unsignedFactor = new UnsignedFactor(pcx);
		unsignedFactor.parse(pcx);

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorのsemanticCheck実行");
        if (unsignedFactor != null) {
            unsignedFactor.semanticCheck(pcx);
            this.setCType(unsignedFactor.getCType());		// unsignedFactor の型をそのままコピー
            this.setConstant(unsignedFactor.isConstant());
        }
    }

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; plusfactor starts");
		if (unsignedFactor != null) { unsignedFactor.codeGen(pcx); }
		o.println(";;; plusfactor completes");
	}
}