package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class MinusFactor extends CParseRule {
	// factor ::= number
	private CParseRule unsignedFactor;
	private CToken op;

	public MinusFactor(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_MINUS;
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
            int type = unsignedFactor.getCType().getType();
            if (type == CType.T_pint) {
                pcx.fatalError("ポインタにーは付けられません");
            }
        }
    }

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("FactorのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; plusfactor starts");
		if (unsignedFactor != null) { unsignedFactor.codeGen(pcx); }
        o.println("\tMOV\t#0, R0\t; MinusFactor:");
        o.println("\tSUB\t-(R6), R0\t; MinusFactor:");
        o.println("\tMOV\tR0, (R6)+\t; MinusFactor:");
        o.println(";;; minusFactor completes");
	}
}