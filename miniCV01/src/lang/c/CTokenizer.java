package lang.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import lang.Tokenizer;

public class CTokenizer extends Tokenizer<CToken, CParseContext> {
	@SuppressWarnings("unused")
	private CTokenRule	rule;
	private int			lineNo, colNo;
	private char		backCh;
	private boolean		backChExist = false;

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
//		System.out.print("'"+ch+"'("+(int)ch+")");
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
//		System.out.println("Token='" + currentTk.toString());
		return currentTk;
	}
	private CToken readToken() {
		CToken tk = null;
		char ch;
		int  startCol = colNo;
		StringBuffer text = new StringBuffer();

		int state = 0;
		boolean accept = false;
		while (!accept) {
			switch (state) {
			case 0:					// 初期状態
				ch = readChar();
				System.out.println("以下の文字で状態遷移"+ch);
				if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				} else if (ch == (char) -1) {	// EOF
					startCol = colNo - 1;
					state = 1;
				} else if (ch >= '0' && ch <= '9') {
					startCol = colNo - 1;
					text.append(ch);
					state = 3;
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
				}
				/*else if(ch == '*'){//
					startCol = colNo -1;
					text.append(ch);
					state = 9;
				}*/else {			// ヘンな文字を読んだ
					startCol = colNo - 1;
					text.append(ch);
					state = 2;
				}
				break;
			case 1:					// EOFを読んだ
				tk = new CToken(CToken.TK_EOF, lineNo, startCol, "end_of_file");
				accept = true;
				break;
			case 2:					// ヘンな文字を読んだ
				tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
				accept = true;
				/*if(ch == '*') {
					//backChar(ch);

					state = 9;
				}else if(ch == (char) -1) {
					System.out.println("EOF");
					state = 0;
					backChar(ch);
					accept = true;
				}else {
					startCol = colNo - 1;
					text.append(ch);
				}*/

				break;
			case 3:					// 数（10進数）の開始

				ch = readChar();
				System.out.println("いま読んだのはなんだ"+ch);
				if (Character.isDigit(ch)) {
					text.append(ch);

				} else {
					// 数の終わり
					backChar(ch);	// 数を表さない文字は戻す（読まなかったことにする）

					tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
					System.out.println(tk);
					System.out.println("TK_NUMは"+CToken.TK_NUM);
					System.out.println("lineNoは"+ lineNo);
					System.out.println("startColは"+startCol);
					System.out.println("text.toStringは"+ text.toString());
					accept = true;
				}
				break;
			case 4:					// +を読んだ
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
				}else if (ch == (char) -1) {//EOF
                    backChar(ch);//読まなかったことにする
                    state = 1;
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

			}


		}
		return tk;
	}
}





