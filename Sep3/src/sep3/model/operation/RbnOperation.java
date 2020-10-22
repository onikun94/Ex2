package sep3.model.operation;
import sep3.model.CPU;

// 通常のADD命令用
public class RbnOperation extends Operation {
	private CPU cpu;
	RbnOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {

		System.out.println("EX0にてRBN命令が呼び出されました");

		if ((cpu.getRegister(CPU.REG_PSW).getValue() & CPU.PSW_N) != 0) {
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
