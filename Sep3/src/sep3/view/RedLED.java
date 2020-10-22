package sep3.view;

// LEDのひとつ分
@SuppressWarnings("serial")

public class RedLED extends LED {
	public RedLED() {
		super("bigLED2red.png", "bigLED2off.png");	// 最初に作るときは消灯状態
	}
}