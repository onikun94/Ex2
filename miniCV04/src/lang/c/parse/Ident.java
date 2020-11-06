package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class Ident extends CParseRule {
    CToken ident;

    public Ident(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_IDENT;
    }


    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("Identのparse実行");
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
        ident = tk;
        ct.getNextToken(pcx);
    }


    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("IdentのsemanticCheck実行");
    	System.out.println("IdentText =="+ident.getType());
    	if(ident.getText().equals("ia_")) {
    		this.setCType(CType.getCType(CType.T_int));

    	}else if(ident.getText().equals("ip_")) {
    		this.setCType(CType.getCType(CType.T_pint));

    	}else if(ident.getText().equals("ia_")) {
    		this.setCType(CType.getCType(CType.T_int_arr));

    	}else if(ident.getText().equals("ipa_")) {
    		this.setCType(CType.getCType(CType.T_pint_arr));

    	}else if(ident.getText().equals("c_")) {
    		this.setConstant(true);
    	}


    }


    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("IdentのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; ident starts");
        if (ident != null) {
            o.println("\tMOV\t#" + ident.getText() + ", (R6)+\t; Ident: 変数アドレスを積む<"+ ident.toExplainString() + ">");
        }
        o.println(";;; ident completes");
    }
}