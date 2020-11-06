package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Primary extends CParseRule {
    private CParseRule child;
    public Primary(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return PrimaryMult.isFirst(tk) | Variable.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("Primaryのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        CToken op = ct.getCurrentToken(pcx);

        if (op.getType() == CToken.TK_MUL) {
            child = new PrimaryMult(pcx);
            child.parse(pcx);
        } else {
            child = new Variable(pcx);
            child.parse(pcx);
        }
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryのsemanticCheck実行");
        if (child != null) {
            child.semanticCheck(pcx);
            this.setCType(child.getCType());
            this.setConstant(child.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primary starts");
        if (child != null) {
            child.codeGen(pcx);
        }
        o.println(";;; primary completes");
    }
}