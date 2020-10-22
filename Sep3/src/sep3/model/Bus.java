package sep3.model;

import sep3.controller.LCDDisplayable;
import sep3.misc.SimObservable;

// SEP-3 のバス
public class Bus extends SimObservable implements LCDDisplayable {
	public static final boolean NeedSelector = true;
	private int			value;							// このバスに現在流れている値
	private Selector	selector;						// どのレジスタの値を選択的に流すか決める回路

	// 各種getterとsetter
	public int			getValue()					{ return value; }
	public void			setValue(int v)				{ value = v; notifyObservers(); } // model が変わったことを通知
	public Selector		getSelector()				{ return selector; }
	public void			setSelector(Selector s)		{ selector = s; }

	public Bus(boolean in, boolean out) {
		// このバスへデータを流すためのセレクタ（in）と、バスから渡すためのセレクタ（out）を登録する
		if (in & out) { selector = new Selector(this, this); }
		else if (in)  { selector = new Selector(this, null); }
		else if (out) { selector = new Selector(null, this); }
	}
	public Bus(CPU cpu, boolean in, boolean out) {
		this(in, out);
		selector.setCPU(cpu);
	}
	// 表示のため、モデルが更新されたかのように扱う
	public void touch()				{ notifyObservers(); }
}
