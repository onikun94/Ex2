package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;

public class AckLampObserver implements SimObserver {
	private Model model;
	private View  view;

	public AckLampObserver(Model m, View v) { model = m; view = v; }

	public void update(Object o) {
		//System.out.println("enter Observer of Ack lamp");
		// モデル上のランプ状態を、そのままビューに伝える
		if (model.getMemory().getAckLamp().isOn()) {
			view.getAckLED().on();
		} else {
			view.getAckLED().off();
		}
	}
}
