package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;
public class Term extends CParseRule {
	// term ::= factor
	private ArrayList<CParseRule> termMultDiv = new ArrayList<CParseRule>();
	private CParseRule multDiv;


	public Term(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Factor.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		System.out.println("Termのparse実行");
		// ここにやってくるときは、必ずisFirst()が満たされている
		CParseRule factor = new Factor(pcx);
		factor.parse(pcx);

		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		termMultDiv.add(factor);

		while (TermMult.isFirst(tk) || TermDiv.isFirst(tk)) {
		  if(tk.getType() == CToken.TK_MUL) {
			  multDiv = new TermMult(pcx);
			  tk = ct.getNextToken(pcx);
			  multDiv.parse(pcx);
			  termMultDiv.add(multDiv);
		  }else if(tk.getType() == CToken.TK_DIV) {
			  multDiv = new TermDiv(pcx);
			  tk = ct.getNextToken(pcx);
			  multDiv.parse(pcx);
			  termMultDiv.add(multDiv);
		  }
		}

	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのsemanticCheck実行");
		 if (termMultDiv.size() >= 2) {
	            boolean isMulDivConstant = false;
	            for (int i = 0; i + 1 <= termMultDiv.size() - 1; i++) {
	                CParseRule left = termMultDiv.get(i);
	                CParseRule right = termMultDiv.get(i + 1);
	                //System.out.println("left="+left+"right="+right);
	                left.semanticCheck(pcx);
	                right.semanticCheck(pcx);
	                int leftType = left.getCType().getType();
	                int rightType = right.getCType().getType();
	                int result = leftType * rightType;
	                if (result != CType.T_int) {
	                    pcx.fatalError("乗除算にはポインタまたはarrayを用いることができません:");
	                }
	                isMulDivConstant = left.isConstant() & right.isConstant();
	            }
	            this.setCType(CType.getCType(CType.T_int)); // 乗除算のときはポインタは使えないため
	            this.setConstant(isMulDivConstant);
	        } else if (termMultDiv.size() == 1) {
	            CParseRule fac = termMultDiv.get(0);
	            fac.semanticCheck(pcx);
	            this.setCType(fac.getCType());		// factor の型をそのままコピー
	            this.setConstant(fac.isConstant());
	        } else {
	            pcx.fatalError("MULT/DIVの後ろにfactorがありません");
	        }
	    }

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		System.out.println("TermのcodeGen実行");
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; term starts");
		if (termMultDiv != null) {
            termMultDiv.stream()
                    .forEach(term -> {
                        try {
                            term.codeGen(pcx);
                        } catch (FatalErrorException e) {
                            e.printStackTrace();
                        }
                    });
        }
		o.println(";;; term completes");
	}
}
