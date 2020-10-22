package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;

public class HLTLampObserver implements SimObserver {
	private Model model;
	private View  view;

	public HLTLampObserver(Model m, View v) { model = m; view = v; }
	public void update(Object o) {
		//System.out.println("enter Observer of HLT lamp");
		// モデル上のHLTランプ状態を、ビュー上のHLTランプに反映させる
		if (model.getCPU().getHaltLamp().isOn()) {
			view.getHltLED().on();
		} else {
			view.getHltLED().off();
		}
	}
}
