package lang.c;

import lang.FatalErrorException;
import lang.IOContext;
import lang.c.parse.Program;

public class MiniCompiler {
	public static void main(String[] args) {
		String inFile = args[0]; // 適切なファイルを絶対パスで与えること
		IOContext ioCtx = new IOContext(inFile, System.out, System.err);
		CTokenizer tknz = new CTokenizer(new CTokenRule());
		CParseContext pcx = new CParseContext(ioCtx, tknz);
		try {
			CTokenizer ct = pcx.getTokenizer();
			CToken tk = ct.getNextToken(pcx);
			if (Program.isFirst(tk)) {
				CParseRule parseTree = new Program(pcx);
				System.out.println("MiniCompilerのparse実行");
				parseTree.parse(pcx);									// 構文解析
				if (pcx.hasNoError()) {
					System.out.println("MiniCompilerのsemanticCheck実行");
					parseTree.semanticCheck(pcx);		// 意味解析
				}
				if (pcx.hasNoError()) {
					/*System.out.println("MiniCompilerのcodeGen実行");
					parseTree.codeGen(pcx);    // コード生成*/
				}
				pcx.errorReport();
			} else {
				pcx.fatalError(tk.toExplainString() + "プログラムの先頭にゴミがあります");
			}
		} catch (FatalErrorException e) {
			e.printStackTrace();
		}
	}
}
