package sep3.model.operation;
import sep3.model.CPU;

// 通常のADD命令用
public class RbzOperation extends Operation {
	private CPU cpu;
	RbzOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {

		if ((cpu.getRegister(CPU.REG_PSW).getValue() & CPU.PSW_Z) != 0) {
			// AバスにMDRの値を、BバスへB0の値を出力する
			useABus(true);
			useBBus(true);

			// 両バスの値を足してSバスに渡す
			int i = cpu.getABus().getValue();
			int j = cpu.getBBus().getValue();
			int o = i + j;

			cpu.getSBus().setValue(o & 0xFFFF);

			writeBack(true);

		}else {
			// AバスもBバスも使わない
			useABus(false);
			useBBus(false);

			// Bバスの値をそのままSバスに出力する
			int i = cpu.getBBus().getValue();
			cpu.getSBus().setValue(i);

			// Sバスの値は捨てる
			writeBack(false);
		}

	}
}
