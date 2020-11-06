package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class PrimaryMult extends CParseRule {
    CParseRule variable;

    public PrimaryMult(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_MUL;
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        if (!Ident.isFirst(tk)) {
            pcx.fatalError(
              String.format("[%s]*(ポインタ)演算子の後ろはIdentifierです", tk.toExplainString()));
        }
        variable = new Variable(pcx);
        variable.parse(pcx);
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのsemanticCheck実行");
        if (variable != null) {
            variable.semanticCheck(pcx);
            int  tp = variable.getCType().getType();
            if (tp == CType.T_int) {
                pcx.fatalError("数値はデリファレンスできません");
            } else if (tp == CType.T_pint) {
                this.setCType(CType.getCType(CType.T_int));
            }
            this.setConstant(variable.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primarymult starts");
        if (variable != null) {
            variable.codeGen(pcx);
        }
        o.println("\tMOV\t-(R6), R0\t; PrimaryMult:番地から値を取り出す");
        o.println("\tMOV\t(R0), (R6)+\t; PrimaryMult:");
        o.println(";;; primarymult completes");
    }
}