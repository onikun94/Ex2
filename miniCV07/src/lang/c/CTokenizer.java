
package lang.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import lang.Tokenizer;

public class CTokenizer extends Tokenizer<CToken, CParseContext> {
    @SuppressWarnings("unused")
    private CTokenRule	rule;
    private int         lineNo, colNo;
    private char        backCh;
    private boolean     backChExist = false;

    public CTokenizer(CTokenRule rule) {
        this.rule = rule;
        lineNo = 1; colNo = 1;
    }

    private InputStream in;
    private PrintStream err;

    private char readChar() {
        char ch;
        if (backChExist) {
            ch = backCh;
            backChExist = false;
        } else {
            try {
               ch = (char) in.read();
            } catch (IOException e) {
                e.printStackTrace(err);
                ch = (char) -1;
            }
        }
        ++colNo;
        if (ch == '\n')  { colNo = 1; ++lineNo; }

        return ch;
    }
    private void backChar(char c) {
        backCh = c;
        backChExist = true;
        --colNo;
        if (c == '\n') { --lineNo; }
    }

    // 現在読み込まれているトークンを返す
    private CToken currentTk = null;
    public CToken getCurrentToken(CParseContext pctx) {
        return currentTk;
    }
    // 次のトークンを読んで返す
    public CToken getNextToken(CParseContext pctx) {
        in = pctx.getIOContext().getInStream();
        err = pctx.getIOContext().getErrStream();
        currentTk = readToken();

        return currentTk;
    }
    private CToken readToken() {
        CToken tk = null;
        char ch;
        int  startCol = colNo;
        StringBuffer text = new StringBuffer();
        boolean isNum = false;

        int state = 0;
        boolean accept = false;
        while (!accept) {
            switch (state) {
            case 0:  // 初期状態
                ch = readChar();
                //System.out.println("以下の文字で状態遷移"+ch);
                if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
                } else if (ch == (char) -1) {	// EOF
                    System.out.println("EOF処理に移動");
                    startCol = colNo - 1;
                    state = 1;
                } else if (ch > '0' && ch <= '9') {
                    System.out.println("数字を扱う");
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 3;
                } else if(ch =='0') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 10;
                } else if (ch == '+') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 4;
                } else if (ch == '-') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 5;
                } else if (ch == '/') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 6;
                } else if(ch == '&'){//
                    startCol = colNo -1;
                    text.append(ch);
                    state = 11;
                } else if(ch == '*') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 12;
                } else if(ch == '(') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 13;
                } else if(ch == ')') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 14;
                }else if (ch == '[') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 16;
                } else if (ch == ']') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 17;
                }else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_')) {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 18;
                }else if (ch == '=') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 19;
                } else if (ch == ';') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 20;
                }else if (ch == '<') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 21;
                } else if (ch == '>') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 22;
                } else if (ch == '!') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 23;
                } else if (ch == '{') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 24;
                } else if (ch == '}') {
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 25;
                } else {             // ヘンな文字を読んだ
                    startCol = colNo - 1;
                    text.append(ch);
                    state = 2;
                }
                break;
            case 1:                 // EOFを読んだ
                System.out.println("EOFについての処理");
                tk = new CToken(CToken.TK_EOF, lineNo, startCol, "end_of_file");
                accept = true;
                break;
            case 2:                 // ヘンな文字を読んだ
                tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
                accept = true;
                break;
            case 3:                // 数（10進数）の開始
                System.out.println("10進数についての処理");
                ch = readChar();
                System.out.println(text.toString());

                if (Character.isDigit(ch)|| (isNum && ((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')))) {
                    text.append(ch);

                } else {
                    try {
                        if (Math.abs(Integer.decode(text.toString())) > 0xFFFF) {
                            tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
                        } else {
                            backChar(ch);
                            tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
                        }
                        accept = true;
                    } catch (NumberFormatException e) {
                        state = 2;
                    }

                }
                break;
            case 4:// +を読んだ
                System.out.println("+の処理");
                tk = new CToken(CToken.TK_PLUS, lineNo, startCol, "+");
                accept = true;
                break;
            case 5:                 // -を読んだ
                tk = new CToken(CToken.TK_MINUS, lineNo, startCol, "-");
                accept = true;
                break;
            case 6:
                //System.out.println("/の処理を行います");
                // 「/」の次の文字によって処理が変わるため条件分岐を用いる.その条件分岐で更なるcaseへ移行
                ch = readChar();//次の文字を読む
                if(ch == '/') {//「//」の時
                    //text.append(ch);
                    state = 7;
                }else if(ch == '*') {//「/*」のとき
                    startCol = colNo - 1;
                    //text.append(ch);
                    state = 8;
                }else if (ch == ' ') { //割り算の次の数字が空白
                    state = 15;
                }else if (ch == (char) -1) {//EOF
                    backChar(ch);//読まなかったことにする
                    state = 1;
                }else {
                    accept = true;
                    backChar(ch);
                    tk = new CToken(CToken.TK_DIV, lineNo, startCol, "/");
                }
                break;
            case 7://「//」の時の処理
                //System.out.println("//の処理を行います");
                ch = readChar();
                //System.out.println(ch);
                if(ch == '\n') {
                    state = 0;
                    text = new StringBuffer();
                    //accept = true;
                }else if(ch == (char) -1){
                    backChar(ch); //読まなかったことにする
                    state = 1;    //EOFの処理
                }

                break;
             case 8://「/*」の時の処理
                //System.out.println("/*の処理を行います");
                ch = readChar();
                //System.out.println(ch);
                if(ch == '*') {
                    state = 9;
                }

                else if(ch == (char) -1){
                    state = 2;
                    System.out.println("エラーです");
                }

                break;
             case 9://「(/*)*」の時の処理
                //System.out.println("(/*)*の処理を行います");
                ch = readChar();
                //System.out.println(ch);
                if(ch == '/') {
                    state = 0;
                    text = new StringBuffer();
                }else if(ch == (char) -1){ // EOF
                    backChar(ch); //読まなかったことにする
                    state = 1;    //EOFの処理
                }else {
                    backChar(ch); //読まなかったことにする
                    state = 8;
                }
                break;
             case 10://16進数と8進数の処理
                System.out.println("16進数と8進数についての処理");
                ch = readChar();
                 if (ch == 'x' || (ch >= '0' && ch <= '9')) {
                     startCol = colNo - 1;
                     isNum = true;
                     text.append(ch);
                     System.out.println("状態3に移行");
                     state = 3;
                 } else {
                     System.out.println("数字以外のとき");
                     /*if(ch == (char) -1) {
                         System.out.println("読まなかったことにする");
                         backChar(ch); //読まなかったことにする
                         state = 1;
                	 }*/
                     backChar(ch);
                     System.out.println(text.toString());
                     tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());

                     accept = true;
                 }
                 break;
            case 11:
                System.out.println("&についての処理");
                tk = new CToken(CToken.TK_AMP, lineNo, startCol, "&");
                accept = true;
                break;
            case 12 :
                System.out.print("*についての処理");
                tk = new CToken(CToken.TK_MUL, lineNo, startCol, "*");
                accept = true;
                break;
            case 13 :
                System.out.println("(についての処理");
                tk = new CToken(CToken.TK_LPAR, lineNo, startCol, "(");
                accept = true;
                break;
            case 14 :
                System.out.println(")についての処理");
                tk = new CToken(CToken.TK_RPAR, lineNo, startCol, ")");
                accept = true;
                break;
            case 15:
            	System.out.println("割り算についての処理");
                ch = readChar();
                if (ch == ' ') {
                } else if (Character.isDigit(ch)
                        || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                    backChar(ch);
                    tk = new CToken(CToken.TK_DIV, lineNo, startCol, "/");
                    accept = true;
                } else {
                    text.append(ch);
                    state = 2;
                }
                break;
            case 16:
                System.out.println("[についての処理");
                tk = new CToken(CToken.TK_LBRA, lineNo, startCol, "[");
                accept = true;
                break;
            case 17:
                System.out.println("]についての処理");
                tk = new CToken(CToken.TK_RBRA, lineNo, startCol, "]");
                accept = true;
                break;
            case 18:
                System.out.println("識別子についての処理");
                ch = readChar();
                 if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')|| (ch == '_') || (ch >= '0' && ch <= '9')) {
                     text.append(ch);
                 }else {
                    try {
                            backChar(ch);
                            tk = new CToken(CToken.TK_IDENT, lineNo, startCol, text.toString());
                            accept = true;
                    	    //ver06の処理
                            /*String s = text.toString();
                            Integer i = (Integer) rule.get(s);
                            tk = new CToken(((i == null) ? CToken.TK_IDENT: i.intValue()), lineNo, startCol, s);
                            accept = true;*/
                            break;
                    } catch (NumberFormatException e) {
                        state = 2;
                    }

                }
                 break;

            case 19:
                 System.out.println("=についての処理");
                 ch = readChar();
                 tk = new CToken(CToken.TK_ASSIGN, lineNo, startCol, "=");
                 if(ch == '=') {
                     tk = new CToken(CToken.TK_EQ, lineNo, startCol,"==");
                 }else {
                     backChar(ch);
                 }
                 accept = true;
                 break;

            case 20:
                System.out.println(";についての処理");
                tk = new CToken(CToken.TK_SEMI, lineNo, startCol, ";");
                accept = true;
                break;

            case 21:
                System.out.println("「<」関連の処理");
                ch = readChar();
                tk = new CToken(CToken.TK_LT,lineNo, startCol, "<");
                if(ch == '=') {
                    tk = new CToken(CToken.TK_LE,lineNo, startCol, "<=");
                }else {
                    backChar(ch);
                }
                accept = true;
                break;

            case 22:
                System.out.println("「>」関連の処理");
                ch = readChar();
                tk = new CToken(CToken.TK_GT,lineNo, startCol, ">");
                if(ch == '=') {
                    tk = new CToken(CToken.TK_GE,lineNo, startCol, ">=");
                }else {
                    backChar(ch);
                }
                accept = true;
                break;

            case 23:
                System.out.println("「!=」の処理");
                ch = readChar();
                if(ch == '=') {
                    tk = new CToken(CToken.TK_NE, lineNo,startCol, "!=");
                    accept = true;
                }else {
                    text.append(ch);
                    state = 2;
                }
                break;

            case 24:
                System.out.println("「{」の処理");
                tk = new CToken(CToken.TK_LCUR, lineNo, startCol, "{");
                accept = true;
                break;


            case 25:
                System.out.println("「}」の処理");
                tk = new CToken(CToken.TK_RCUR, lineNo, startCol, "}");
                accept = true;
                break;
            }


        }
        return tk;
   }
}