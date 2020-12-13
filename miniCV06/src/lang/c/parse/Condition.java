package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class Condition extends CParseRule {

	private CParseRule condition;
	private CParseRule expression;
	private boolean isTrue;

	public Condition(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return Expression.isFirst(tk) || tk.getType() == CToken.TK_TRUE || tk.getType() == CToken.TK_FALSE;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
        System.out.println("Conditionのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        if(Expression.isFirst(tk)) {
            expression = new Expression(pcx);
            expression.parse(pcx);

            tk = ct.getCurrentToken(pcx);

            if (ConditionLT.isFirst(tk)) {
                condition = new ConditionLT(expression);
            } else if (ConditionLE.isFirst(tk)) {
                condition = new ConditionLE(expression);
            } else if (ConditionGT.isFirst(tk)) {
                condition = new ConditionGT(expression);
            } else if (ConditionGE.isFirst(tk)) {
                condition = new ConditionGE(expression);
            } else if (ConditionEQ.isFirst(tk)) {
                condition = new ConditionEQ(expression);
            } else if (ConditionNE.isFirst(tk)) {
                condition = new ConditionNE(expression);
            } else {
                pcx.fatalError(tk.toExplainString() + "比較演算子がありません");
            }
            condition.parse(pcx);

        }else if(tk.getType() == CToken.TK_TRUE) {
        	isTrue = true;
        	ct.getNextToken(pcx);

        }else if(tk.getType() == CToken.TK_FALSE) {
        	isTrue = false;
        	ct.getNextToken(pcx);
        }


    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        System.out.println("ConditionのsemanticCheck実行");
        if (condition != null) {
            condition.semanticCheck(pcx);
            setCType(condition.getCType());
            setConstant(condition.isConstant());
            assert condition.isConstant() : "conditionは絶対に定数";
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        System.out.println("ConditionのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; condition starts");
        if (condition != null) { condition.codeGen(pcx); }
        if(isTrue) {
        	o.println("\tMOV\\t#0x0001, (R6)+\t; Condition: true(1)を積む");
        }else {
        	o.println("\tMOV\t#0x0000, (R6)+\t; Condition: false(0)を積む");
        }

        o.println(";;; condition completes");
    }
}


class ConditionLT extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionLT(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_LT;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionLTのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionLTのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionLTのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionLT: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionLT:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionLT: set true");
	            o.println("\tCMP\tR0, R1\t; ConditionLT: R1 < R0 = R1-R0 < 0");
	            o.println("\tBRN\tLT" + seq + " ; ConditionLT:");
	            o.println("\tCLR\tR2\t\t; ConditionLT: set false");
	            o.println("LT" + seq + ":\tMOV\tR2, (R6)+\t; ConditionLT:");
	        }
	        o.println(";;; condition < (compare) completes");
	 }
}


class ConditionLE extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionLE(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_LE;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionLEのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionLEのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionLEのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionLE: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionLE:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionLE: set true");
	            o.println("\tCMP\tR0, R1\t; ConditionLE: R1 <= R0 = R1-R0 <= 0");
	            o.println("\tBRZ\tLE" + seq + " ; ConditionLE:");
	            o.println("\tBRN\tLE" + seq + " ; ConditionLE:");
	            o.println("\tCLR\tR2\t\t; ConditionLE: set false");
	            o.println("LE" + seq + ":\tMOV\tR2, (R6)+\t; ConditionLE:");
	        }
	        o.println(";;; condition <= (compare) completes");
	 }
}


class ConditionGT extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionGT(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_GT;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionGTのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionGTのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionGTのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionGT: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionGT:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionGT: set true");
	            o.println("\tCMP\tR1, R0\t; ConditionGT: R1 > R0 = R0-R1 < 0");
	            o.println("\tBRN\tGT" + seq + " ; ConditionGT:");
	            o.println("\tCLR\tR2\t\t; ConditionGT: set false");
	            o.println("GT" + seq + ":\tMOV\tR2, (R6)+\t; ConditionGT:");
	        }
	        o.println(";;; condition > (compare) completes");
	 }
}


class ConditionGE extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionGE(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_GE;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionGEのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionGEのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionGEのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionGE: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionGE:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionGE: set true");
	            o.println("\tCMP\tR1, R0\t; ConditionGE: R1 >= R0 = R0-R1 <= 0");
	            o.println("\tBRZ\tGE" + seq + " ; ConditionGE:");
	            o.println("\tBRN\tGE" + seq + " ; ConditionGE:");
	            o.println("\tCLR\tR2\t\t; ConditionGE: set false");
	            o.println("GE" + seq + ":\tMOV\tR2, (R6)+\t; ConditionGE:");
	        }
	        o.println(";;; condition >= (compare) completes");
	 }
}


class ConditionEQ extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionEQ(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_EQ;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionEQのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionEQのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionEQのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionEQ: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionEQ:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionLT: set true");
	            o.println("\tCMP\tR0, R1\t; ConditionEQ: R1 == R0 = R1-R0 == 0");
	            o.println("\tBRZ\tEQ" + seq + " ; ConditionEQ:");
	            o.println("\tCLR\tR2\t\t; ConditionEQ: set false");
	            o.println("EQ" + seq + ":\tMOV\tR2, (R6)+\t; ConditionEQ:");
	        }
	        o.println(";;; condition == (compare) completes");
	 }
}


class ConditionNE extends CParseRule {
	private CToken op;
	private CParseRule left;
	private CParseRule right;
	private int seq;

	public ConditionNE(CParseRule left) {
		this.left = left;
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_NE;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
        // ここにやってくるときは、必ずisFirst()が満たされている
		System.out.println("ConditionNEのparse実行");
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if (Expression.isFirst(tk)) {
            right = new Expression(pcx);
            right.parse(pcx);
        }else {
        	pcx.fatalError(tk.toExplainString() + "比較演算子の後ろには式がきます");
        }

    }



	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		System.out.println("ConditionNEのsemanticCheck実行");
        if (left != null && right != null) {
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);

            if (!left.getCType().equals(right.getCType())) {
                pcx.fatalError(op.toExplainString()+"左辺の型["+left.getCType().toString()+"]と右辺の型["
                +right.getCType().toString()+"]が一致しないので比較できません");

            }else {
                this.setCType(CType.getCType(CType.T_bool));
                this.setConstant(true);
            }

        }
	}

	 public void codeGen(CParseContext pcx) throws FatalErrorException {
		 System.out.println("ConditionNEのcodeGen実行");
	        PrintStream o = pcx.getIOContext().getOutStream();
	        o.println(";;; condition < (compare) starts");
	        if (left != null && right != null) {
	            left.codeGen(pcx);
	            right.codeGen(pcx);
	            seq = pcx.getSeqId();
	            o.println("\tMOV\t-(R6), R0\t; ConditionNE: 2数を取り出して, 比べる");
	            o.println("\tMOV\t-(R6), R1\t; ConditionNE:");
	            o.println("\tMOV\t#0x0001, R2\t; ConditionNE: set true");
	            o.println("\tCMP\tR0, R1\t; ConditionNE: R1 != R0 = R1-R0 != 0");
	            o.println("\tCLR\tR2\t\t; ConditionNE: set false");
	            o.println("NE" + seq + ":\tMOV\tR2, (R6)+\t; ConditionNE:");
	        }
	        o.println(";;; condition != (compare) completes");
	 }
}



