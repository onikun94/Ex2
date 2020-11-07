package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class Variable extends CParseRule {
    CParseRule identArray;

    public Variable(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return Ident.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("Variableのparse実行");
        identArray = new Ident(pcx);
        identArray.parse(pcx);

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		System.out.println("VariableText =="+tk.getText());
		System.out.println("tk.getType =="+tk.getType());
		System.out.println("CToken.TKLBRA =="+CToken.TK_LBRA);

		//identArray = new Array(pcx);
        //identArray.parse(pcx);

        /*if(tk.getType() == CToken.TK_IDENT) {
        	identArray = new Ident(pcx);
            identArray.parse(pcx);
        }*/
        if (tk.getType() == CToken.TK_LBRA) {
            identArray = new Array(pcx);
            identArray.parse(pcx);
        }

    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("VariableのsemanticCheck実行");
        if (identArray != null) {
            identArray.semanticCheck(pcx);
            this.setCType(identArray.getCType());
            this.setConstant(identArray.isConstant());
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("VariableのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; variable starts");
        if (identArray != null) {
            identArray.codeGen(pcx);
        }
        o.println(";;; variable completes");
    }
}