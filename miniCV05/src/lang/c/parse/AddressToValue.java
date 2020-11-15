package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;

public class AddressToValue extends CParseRule {
    CParseRule primary;
    private CToken op;

    public AddressToValue(CParseContext pcx) {
    }

    public static boolean isFirst(CToken tk) {
        return Primary.isFirst(tk);
    }

    @Override
    public void parse(CParseContext pcx) throws FatalErrorException {
    	System.out.println("AddressToValueのparse実行");

        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);
        System.out.println("PrimaryText(AddressToValue.java) =="+op.getText());

        primary = new Primary(pcx);
        primary.parse(pcx);
    }

    @Override
    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    	System.out.println("AddressToValueのsemanticCheck実行");
        if (primary != null) {
            primary.semanticCheck(pcx);
            this.setCType(primary.getCType());
            this.setConstant(false);
        }
    }

    @Override
    public void codeGen(CParseContext pcx) throws FatalErrorException {
    	System.out.println("AddressToValueのcodeGen実行");
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; addresstovalue starts");
        if (primary != null) {
            primary.codeGen(pcx);
        }
        o.println("\tMOV\t-(R6), R0\t; addressToValue:番地から値を取り出す");
        o.println("\tMOV\t(R0), (R6)+\t; addressToValue:");
        o.println(";;; addresstovalue completes");
    }
}