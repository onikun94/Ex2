package sep3.model.operation;
import sep3.model.CPU;

// 通常のADD命令用
public class RbcOperation extends Operation {
	private CPU cpu;
	RbcOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {

		if ((cpu.getRegister(CPU.REG_PSW).getValue() & CPU.PSW_C) != 0) {
			// AバスにMDRの値を、BバスへB0の値を出力する
			useABus(true);
			useBBus(true);

			// 両バスの値を足してSバスに渡す
			int i = cpu.getABus().getValue();
			int j = cpu.getBBus().getValue();
			int o = i + j;

			cpu.getSBus().setValue(o & 0xFFFF);

			writeBack(true);

		}

	}
}
