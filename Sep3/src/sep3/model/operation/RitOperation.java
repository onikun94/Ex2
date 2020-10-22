package sep3.model.operation;
import sep3.model.CPU;

public class RitOperation extends Operation {
	private CPU cpu;
	RitOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {


		if(cpu.getDecoder().getFromMode() == 0X2 && cpu.getDecoder().getFromRegister() == 6) {
			System.out.println("EX0にてRET命令が呼び出されました");


			// Aバスは使わず、B0の値をBバスへ出力する
			useABus(false);
			useBBus(true);

			// Bバスの値をそのままSバスに出力する
			int i = cpu.getBBus().getValue();
			cpu.getSBus().setValue(i);

			// Sバスの値をToオペランドに書き込む
			writeBack(true);

		}


	}
}
