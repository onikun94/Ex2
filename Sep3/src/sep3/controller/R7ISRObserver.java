package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;
import sep3.model.Register;

public class R7ISRObserver implements SimObserver {
	private Model model;
	private View  view;
	private boolean when;

	public R7ISRObserver(Model m, View v, boolean w) {
		model = m; view = v; when = w;
	}

	public void update(Object o) {
		//System.out.println("enter Observer of Register #" + rn);
		// モデル上でレジスタ値が変更されたら、該当するLEDアレイの表示を変更する。
		// ただし、表示切り替えスイッチがwhenの時に限る。
		Register r = (Register) o;
		if (model.getDispSW().isOn() == when) {
			view.getR7ISR().setValue(r.getValue());
		}
	}
}
