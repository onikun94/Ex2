package sep3.controller;

import sep3.Model;
import sep3.View;
import sep3.misc.SimObserver;
import sep3.model.runmode.RunModeFactory;

public class RunModeObserver implements SimObserver {
	private Model model;
	private View  view;

	public RunModeObserver(Model m, View v) { model = m; view = v; }

	public void update(Object o) {
		//System.out.println("enter Observer of RumMode");
		// モデル上の走行状態を、ビュー上の走行モードランプに反映させる
		if (model.getPowerSW().isOn()) {
			switch (model.getRunMode().getID()) {
			case RunModeFactory.RM_NORMAL:
				view.getNormalRunLED().on();
				view.getInstRunLED().off();
				view.getClockRunLED().off();
				break;
			case RunModeFactory.RM_INST:
				view.getNormalRunLED().off();
				view.getInstRunLED().on();
				view.getClockRunLED().off();
				break;
			case RunModeFactory.RM_CLOCK:
				view.getNormalRunLED().off();
				view.getInstRunLED().off();
				view.getClockRunLED().on();
				break;
			default:
				view.getNormalRunLED().off();
				view.getInstRunLED().off();
				view.getClockRunLED().off();
				break;
			}
		} else {
			view.getNormalRunLED().off();
			view.getInstRunLED().off();
			view.getClockRunLED().off();
		}
	}
}
