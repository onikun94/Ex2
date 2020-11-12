package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class Variable extends CParseRule {
    private CParseRule ident;
    private CParseRule array;

    public Variable(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return Ident.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("Variableのparse実行");
        ident = new Ident(pcx);
        ident.parse(pcx);

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		/*System.out.println("VariableText =="+tk.getText());
		System.out.println("tk.getType =="+tk.getType());
		System.out.println("CToken.TKLBRA =="+CToken.TK_LBRA);*/

        if (Array.isFirst(tk)) {
        	System.out.println("arrayの処理に移動");
            array = new Array(pcx);
            array.parse(pcx);
        }


    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("VariableのsemanticCheck実行");
    	System.out.println(ident);
    	//final int Arr = array.getCType().getType();//ここでとまっている


        if (ident != null) {
            ident.semanticCheck(pcx);

            this.setCType(ident.getCType());

            this.setConstant(ident.isConstant());

            System.out.println(array);

            System.out.println(ident.getCType().getType());

            if(array != null) {

              if ((ident.getCType().getType() != CType.T_int_arr) || (ident.getCType().getType() != CType.T_pint_arr)) {
                  pcx.fatalError("arrayに識別子が指定されていません");
              }

              if(ident.getCType().getType() == CType.T_int_arr) {
            	  //ArrayのsemanticCheck
            	  array.semanticCheck(pcx);
            	  //ArrayのsetType
            	  this.setCType(CType.getCType(CType.T_int));

              }else if(ident.getCType().getType() == CType.T_pint_arr) {

            	  array.semanticCheck(pcx);
            	  this.setCType(CType.getCType(CType.T_pint));
              }

            }


        }
        System.out.println("Variableのsemantic終了");
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("VariableのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; variable starts");
        if (ident != null) {
            ident.codeGen(pcx);
            o.println("\tMOV\t-(R6), R1\t; Arrayのexprの値をスタックへ");
			o.println("\tMOV\t-(R6), R0\t; Identのアドレスをポップ");
			o.println("\tADD\tR1, R0   \t; Variableのアドレス値を計算");
			o.println("\tMOV\tR0, (R6)+\t; 計算したアドレスをスタックへ");
        }else {
			o.println("\t\t\t\t; Identのアドレスがスタックに載っているので何もしない");
		}
        o.println(";;; variable completes");
    }
}