package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Primary extends CParseRule {
    private CParseRule mulVar;
    public boolean isMultPrimary = false;
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
        System.out.println("PrimaryText =="+op.getText());

        if (op.getType() == CToken.TK_MUL) {
            mulVar = new PrimaryMult(pcx);
            isMultPrimary = true;
            mulVar.parse(pcx);
        } else {
            mulVar = new Variable(pcx);
            isMultPrimary = false;
            mulVar.parse(pcx);
        }
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryのsemanticCheck実行");
        if (mulVar != null) {
            mulVar.semanticCheck(pcx);
            this.setCType(mulVar.getCType());
            this.setConstant(mulVar.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("PrimaryのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primary starts");
        if (mulVar != null) {
            mulVar.codeGen(pcx);
        }
        o.println(";;; primary completes");
    }
}