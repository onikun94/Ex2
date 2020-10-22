package sep3.model.operation;
import sep3.model.CPU;

public class BrcOperation extends Operation {
	private CPU cpu;
	BrcOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {
		if ((cpu.getRegister(CPU.REG_PSW).getValue() & CPU.PSW_C) != 0) {
			// Aバスは使わない、BバスへB0の値を出力する
			useABus(false);
			useBBus(true);

			// Bバスの値をそのままSバスへ渡す
			cpu.getSBus().setValue(cpu.getBBus().getValue());

			// Sバスの値をToオペランドに書き込む
			writeBack(true);

		}
	}
}
