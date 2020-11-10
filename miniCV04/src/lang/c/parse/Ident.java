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
        if (tk.getType() != CToken.TK_IDENT) {
            pcx.fatalError("識別子がありません");
        }
        ident = tk;
        ct.getNextToken(pcx);
    }


    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("IdentのsemanticCheck実行");
    	System.out.println("IdentText =="+ident.getText());
    	if(ident.getText().startsWith("i_")) {
    		this.setCType(CType.getCType(CType.T_int));
    		System.out.println("i_のときのT_intの処理");

    	}else if(ident.getText().startsWith("ip_")) {
    		this.setCType(CType.getCType(CType.T_pint));
    		System.out.println("ip_のときのT_pintの処理");

    	}else if(ident.getText().startsWith("ia_")) {
    		this.setCType(CType.getCType(CType.T_int_arr));
    		System.out.println("ia_のときのT_int_arrの処理");

    	}else if(ident.getText().startsWith("ipa_")) {
    		this.setCType(CType.getCType(CType.T_pint_arr));
    		System.out.println("ipa_のときのT_pint_arrの処理");

    	}else if(ident.getText().startsWith("c_")) {
    		this.setCType(CType.getCType(CType.T_int));
    		this.setConstant(true);
    	}else {
    		   this.setCType(CType.getCType(CType.T_err));
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