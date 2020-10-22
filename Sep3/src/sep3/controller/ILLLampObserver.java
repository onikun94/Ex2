package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;

public class ILLLampObserver implements SimObserver {
	private Model model;
	private View  view;

	public ILLLampObserver(Model m, View v) { model = m; view = v; }
	public void update(Object o) {
		//System.out.println("enter Observer of ILL lamp");
		// モデル上のILLランプの状態を、ビュー上のILLランプに反映させる
		if (model.getCPU().getIllLamp().isOn()) {
			view.getIllLED().on();
		} else {
			view.getIllLED().off();
		}
	}
}
