package sep3.model.operation;
import sep3.model.CPU;

public class ClrOperation extends Operation {
	private CPU cpu;
	ClrOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {
		// Aバスは使わず、B0の値をBバスへ出力する
		useABus(false);
		useBBus(false);

		// Bバスの値をそのままSバスに出力する
		//int i = cpu.getBBus().getValue();
		int zero = 0x0000;
		cpu.getSBus().setValue(zero);

		// Sバスの値をToオペランドに書き込む
		writeBack(true);

		// PSWの更新: MOVの演算結果（つまり、Bバスの値）に応じてNZのビットを立てる

		int p = psw_NZ(zero);
		p |= cpu.getRegister(CPU.REG_PSW).getValue();
		cpu.getRegister(CPU.REG_PSW).setValue(p);
	}
}
