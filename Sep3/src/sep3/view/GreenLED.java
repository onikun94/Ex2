package sep3.view;

// LEDのひとつ分
@SuppressWarnings("serial")

public class GreenLED extends LED {
	public GreenLED() {
		super("bigLED2green.png", "bigLED2off.png");	// 最初に作るときは消灯状態
	}
}