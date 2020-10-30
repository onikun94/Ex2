package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Program extends CParseRule {
	// program ::= expression EOF
	private CParseRule program;

	public Program(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Expression.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("Programのparse実行");
		program = new Expression(pcx);
		System.out.println("program="+program);
		program.parse(pcx);
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		System.out.println("現在のタイプは"+tk.getType());
		System.out.println("EOFは"+CToken.TK_EOF);
		if (tk.getType() != CToken.TK_EOF) {
			pcx.fatalError(tk.toExplainString() + "プログラムの最後にゴミがあります");
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ProgramのsemanticCheck実行");
		if (program != null) { program.semanticCheck(pcx); }
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("ProgramのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; program starts");
		o.println("\t. = 0x100");
		o.println("\tJMP\t__START\t; ProgramNode: 最初の実行文へ");
		// ここには将来、宣言に対するコード生成が必要
		if (program != null) {
			o.println("__START:");
			o.println("\tMOV\t#0x1000, R6\t; ProgramNode: 計算用スタック初期化");
			program.codeGen(pcx);
			o.println("\tMOV\t-(R6), R0\t; ProgramNode: 計算結果確認用");
		}
		o.println("\tHLT\t\t\t; ProgramNode:");
		o.println("\t.END\t\t\t; ProgramNode:");
		o.println(";;; program completes");
	}
}
