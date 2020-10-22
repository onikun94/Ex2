package sep3.model.runmode;

import sep3.misc.SimObservable;

// CPU走行モードを変更した場合、その事実をモデルからビューに伝えるためのクラス
public class ObservableRunMode extends SimObservable { //Observable {
	private RunMode rm;
	public void setRunMode(RunMode m)	{ rm = m; notifyObservers(); }
	public RunMode getRunMode()			{ return rm; }
}
