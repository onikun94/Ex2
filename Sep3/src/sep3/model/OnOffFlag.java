package sep3.model;

import sep3.misc.SimObservable;

// モデル上のHLT, ILL状態をビューに通知するための変数
public class OnOffFlag extends SimObservable {
	private boolean on = false;   // 起動時はoff

	// フラグの変更をビューに通知する
	public void toggle()  { on = !on;    notifyObservers(); }
	public void on()      { on = true;  notifyObservers(); }
	public void off()     { on = false; notifyObservers(); }
	public boolean isOn() { return on; }
}
