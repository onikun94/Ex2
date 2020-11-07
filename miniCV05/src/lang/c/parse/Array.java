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
    	System.out.println("Arrayのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        //CToken tk = ct.getCurrentToken(pcx);
       // System.out.println("ArrayText =="+tk.getText());
        ct.getNextToken(pcx);
        expression = new Expression(pcx);
        expression.parse(pcx);

        CToken tk = ct.getCurrentToken(pcx);
        System.out.println("ArrayText =="+tk.getText());
        if (tk.getType() != CToken.TK_RBRA) {
            pcx.fatalError("[]が閉じていません");
        }
        ct.getNextToken(pcx);
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("ArrayのsemanticCheck実行");
        if (expression != null) {
            expression.semanticCheck(pcx);
            CType ty = expression.getCType();
            if (ty.getType() != CType.T_int) {
                pcx.fatalError("インデックス番号はintのみ使用可能です");
            }
            this.setCType(ty);
            this.setConstant(expression.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("ArrayのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; array starts");
        if (expression != null) {
            expression.codeGen(pcx);
        }
        o.println(";;; array completes");
    }
}