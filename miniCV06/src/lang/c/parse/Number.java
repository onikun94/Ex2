package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class Number extends CParseRule {
	// number ::= NUM
	private CToken num;
	public Number(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_NUM;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Numberのparse実行");
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		num = tk;
		System.out.println("Number0Textのトークンの綴りは"+tk.getText());
		tk = ct.getNextToken(pcx);
		System.out.println("NumberTextのトークンの綴りは"+tk.getText());
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("NumberのsemanticCheck実行");
		this.setCType(CType.getCType(CType.T_int));
		this.setConstant(true);
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("NumberのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; number starts");
		if (num != null) {
			o.println("\tMOV\t#" + num.getText() + ", (R6)+\t; Number: 数を積む<" + num.toExplainString() + ">");
		}
		o.println(";;; number completes");
	}
}
