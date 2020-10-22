package sep3.model.operation;
import sep3.model.CPU;

// 通常のSUB命令用
public class OrOperation extends Operation {
	private CPU cpu;
	OrOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {
		// AバスにMDRの値を、BバスへB0の値を出力する
		useABus(true);
		useBBus(true);

		// 両バスの値を引いてSバスに渡す。ToオペランドからFromオペランドをひく。
		int i = cpu.getABus().getValue();
		int j = cpu.getBBus().getValue();
		int o = (i | j);

		// PSWの更新
		int p = psw_NZ(o);

		cpu.getRegister(CPU.REG_PSW).setValue(p);

		// キャリーがあったら捨てて、Sバスの値をToオペランドに書き込む
		cpu.getSBus().setValue(o & 0xFFFF);
		writeBack(true);
	}
}
