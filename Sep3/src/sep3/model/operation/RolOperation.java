package sep3.model.operation;
import sep3.model.CPU;

public class RolOperation extends Operation {
	private CPU cpu;
	RolOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {
		// Bバスは使わず、MDRの値をAバスへ出力する
		useABus(true);
		useBBus(false);

		//Aバスの値を取得
		int i = cpu.getABus().getValue();
		boolean j = bit(i ,0x8000);
		//2倍すると桁が移動する,最下位ビットは必ず0になるから桁移動させる前の最上位ビットが1の場合を考慮する必要がある.
		int k = i * 0x2;
		int o;
		if(j) {
			o = (k | 0x0001);
		}else {
			o = k;
		}
		//int k = (i & 0x8000 | (j & 0x7FFF));//0x8000は2進数で1000000000000000

		int l = psw_NZ(o);

		  boolean n = bit(i,0x8000);

		  if(n) {l |= CPU.PSW_C;}

			// C will not change
		cpu.getRegister(CPU.REG_PSW).setValue(l & 0xFFFF);
		// Sバスの値をToオペランドに書き込む
		cpu.getSBus().setValue(k & 0xFFFF);
		writeBack(true);
	}
}
