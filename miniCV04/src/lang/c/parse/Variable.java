package lang.c.parse;

import java.io.PrintStream;
import java.util.Optional;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Variable extends CParseRule {
    CParseRule ident;
    Optional<CParseRule> array;

    public Variable(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return Ident.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
        ident = new Ident(pcx);
        ident.parse(pcx);
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
        //final var tokenizer = pcx.getTokenizer();
        //var token = tokenizer.getCurrentToken(pcx);
        CParseRule tmp_array = null;
        if (tk.getType() == CToken.TK_LBRA) {
            tmp_array = new Array(pcx);
            tmp_array.parse(pcx);
        }
        array = Optional.ofNullable(tmp_array);
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();

    }
}