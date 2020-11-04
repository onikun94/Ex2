package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Primary extends CParseRule {
    private CParseRule child;
    public boolean isMultPrimary = false;
    public Primary(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return PrimaryMult.isFirst(tk) | Variable.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
        //final var tokenizer = pcx.getTokenizer();
        CTokenizer ct = pcx.getTokenizer();
        CToken op = ct.getCurrentToken(pcx);
        //final var token = tokenizer.getCurrentToken(pcx);
        if (op.getType() == CToken.TK_MUL) {
            child = new PrimaryMult(pcx);
            isMultPrimary = true;
            child.parse(pcx);
        } else {
            child = new Variable(pcx);
            isMultPrimary = false;
            child.parse(pcx);
        }

    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if (child != null) {
            child.semanticCheck(pcx);
            this.setCType(child.getCType());
            this.setConstant(child.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primary starts");
        if (child != null) {
            child.codeGen(pcx);
        }
        o.println(";;; primary completes");
    }
}