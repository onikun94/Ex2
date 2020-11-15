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
		System.out.println("VariableTextのトークンの綴りは"+tk.getText());
		/*System.out.println("tk.getType =="+tk.getType());
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
    	System.out.println("変数identは"+ident+"に遷移");
    	//final int Arr = array.getCType().getType();//ここでとまっている


        if (ident != null) {
            ident.semanticCheck(pcx);

            this.setCType(ident.getCType());

            this.setConstant(ident.isConstant());

            System.out.println("変数arrayは"+array+"に遷移");

            System.out.println("識別子の型は"+ident.getCType().getType());

              if ((ident.getCType().getType() == CType.T_int_arr) || (ident.getCType().getType() == CType.T_pint_arr)) {
            	  if(ident.getCType().getType() == CType.T_int_arr) {
            		  System.out.println("配列がint型の場合");
                   	  if(Primary.isMultPrimary == true) {
                		  pcx.fatalError("*ia_a		// 不当(Cでは正当)");
                	  }
            		  else if(array == null) {
                		  pcx.fatalError("array型に[]が指定されていません");
                	  }
                	  //ArrayのsemanticCheck
                	  array.semanticCheck(pcx);
                	  //ArrayのsetType
                	  this.setCType(CType.getCType(CType.T_int));

                  }else if(ident.getCType().getType() == CType.T_pint_arr) {
                	  System.out.println("配列がpint型の場合");
                	  if(Primary.isMultPrimary == true) {
                		  pcx.fatalError("*ipa_a		// 不当(Cでは正当)");
                	  }
                	  else if(array == null) {
                		  pcx.fatalError("array型に[]が指定されていません");
                	  }
                	  array.semanticCheck(pcx);
                	  this.setCType(CType.getCType(CType.T_pint));
                  }

              }

              if(array != null) {
                  if((ident.getCType().getType() == CType.T_pint ) ||(ident.getCType().getType() == CType.T_int )) {
                	  pcx.fatalError("配列の識別子はint[]型かint*[]型でないといけません");
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