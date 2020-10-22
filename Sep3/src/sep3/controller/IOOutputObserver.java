package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;

public class IOOutputObserver implements SimObserver {
	private Model model;
	private View  view;

	public IOOutputObserver(Model m, View v) { model = m; view = v; }
	public void update(Object o) {
		//System.out.println("enter Observer DataOutputLED(IOOutputObserver: " + i);
		// モデル上で出力行為があったら、その値を取り出してビュー上の出力LEDを点灯させる
		int i = model.getMemory().getIOValue().getValue();
		view.getDataOutputLED().setData(i);
	}
}
