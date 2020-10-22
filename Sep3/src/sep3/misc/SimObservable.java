package sep3.misc;

import java.util.ArrayList;

public class SimObservable {
	// このオブジェクトを監視している監視者たちのリスト
	private ArrayList<SimObserver> obs = new ArrayList<SimObserver>();

	// 監視者の追加と削除
	public void addObserver(SimObserver o) { obs.add(o); }
	public void deleteObserver(SimObserver o) { obs.remove(o); }

	// 監視者全員に通知
	public void notifyObservers() {
		for (SimObserver o : obs) { o.update(this); }
	}
}
