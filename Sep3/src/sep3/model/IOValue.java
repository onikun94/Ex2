package sep3.model;

import sep3.misc.SimObservable;

// メモリマップトI/OでLEDに出力したい場合、その事実をモデルからビューに伝えるための変数
public class IOValue extends SimObservable { //Observable {
	private int value;

	public void setValue(int v) {
		value = v;
		notifyObservers(); // 出力すべきものが来たので、ビューに知らせて表示してもらう
	}
	public int  getValue() { return value; }
}
