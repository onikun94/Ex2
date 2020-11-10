package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class PrimaryMult extends CParseRule {
	private CToken op;
	//private CToken tk;
    private CParseRule child;

    public PrimaryMult(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_MUL;
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        //tk = ct.getCurrentToken(pcx);
        //System.out.println("tk.getType_Primarrytk =="+tk.getType());
        op = ct.getNextToken(pcx);
        //System.out.println("tk.getType_Primarry =="+op.getType());
        if (!Ident.isFirst(op)) {
            pcx.fatalError(
              String.format("[%s]*(ポインタ)の後ろは識別子です", op.toExplainString()));
        }
        child = new Variable(pcx);
        child.parse(pcx);

    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのsemanticCheck実行");
        if (child != null) {
            child.semanticCheck(pcx);
            final int  tp = child.getCType().getType();
            if (tp == CType.T_int) {
                pcx.fatalError("数値は参照できません");
            } else if (tp == CType.T_pint) {
                this.setCType(CType.getCType(CType.T_int));
            }
            this.setConstant(child.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryMultのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primarymult starts");
        if (child != null) {
            child.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; PrimaryMult: アドレスを取り出して、内容を参照して、積む<"
            		+ op.toExplainString() + ">");
            o.println("\tMOV\t(R0), (R6)+\t; PrimaryMult:");
            o.println(";;; primarymult completes");
        }


    }
}