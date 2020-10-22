package sep3.model.operation;
import sep3.model.CPU;

// 通常のADD命令用
public class RjpOperation extends Operation {
	private CPU cpu;
	RjpOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {

		System.out.println("EX0にてRJP命令が呼び出されました");

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
