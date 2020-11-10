package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

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
		/*System.out.println("VariableText =="+tk.getText());
		System.out.println("tk.getType =="+tk.getType());
		System.out.println("CToken.TKLBRA =="+CToken.TK_LBRA);*/

        if (Array.isFirst(tk)) {
            identArray = new Array(pcx);
            identArray.parse(pcx);
        }


    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("VariableのsemanticCheck実行");
    	System.out.println(identArray);
    	final int Arr = identArray.getCType().getType();//ここでとまっている
    	System.out.println("ttttttt");

        if (identArray != null) {
            identArray.semanticCheck(pcx);

            if ((Arr == CType.T_int_arr) || (Arr == CType.T_pint_arr)) {

                pcx.fatalError("arrayに識別子が指定されていません");
            }

            if(Arr == CType.T_int_arr) {
            	this.setCType(CType.getCType(CType.T_int));
            }else {
            	this.setCType(CType.getCType(CType.T_pint));
            }

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