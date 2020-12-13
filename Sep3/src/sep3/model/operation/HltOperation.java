package sep3.model.operation;
import sep3.model.CPU;

public class HltOperation extends Operation {
	private CPU cpu;
	HltOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {
		System.out.println("EX0にてHLT命令が呼び出されました");
		// AバスもBバスも使わない
		useABus(false);
		useBBus(false);

		// Sバスには、Aバスの値をそのまま渡す（Aバスは使わないので0になっている）
		cpu.getSBus().setValue(cpu.getABus().getValue());

		// Sバスの値は捨てる//(実験Ⅲで確認)
		writeBack(true);

		// HLT状態にする(フラグを立てる)
		cpu.getHaltLamp().on();
	}
}
