package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class Array extends CParseRule {
    CParseRule expression;

    public Array(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_LBRA;
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        ct.getNextToken(pcx);
        expression = new Expression(pcx);
        expression.parse(pcx);
        CToken tk = ct.getCurrentToken(pcx);
        if (tk.getType() != CToken.TK_RBRA) {
            pcx.fatalError("arrayの[]が閉じていません");
        }
        ct.getNextToken(pcx);
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if (expression != null) {
            expression.semanticCheck(pcx);
            CType ty = expression.getCType();
            if (ty.getType() != CType.T_int) {
                pcx.fatalError("配列のインデックスにはintしか使えません");
            }
            this.setCType(ty);
            this.setConstant(expression.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; array starts");
        if (expression != null) {
            expression.codeGen(pcx);
        }
        o.println("\tMOV\t-(R6), R0\t; Array:配列が示している番地を計算し、格納します");
        o.println("\tADD\t-(R6), R0\t; Array:");
        o.println("\tMOV\tR0, (R6)+\t; Array:");
        o.println(";;; array completes");
    }
}