package sep3.model.operation;
import sep3.model.CPU;

public class AslOperation extends Operation {
	private CPU cpu;
	AslOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {
		// Bバスは使わず、MDRの値をAバスへ出力する
		useABus(true);
		useBBus(false);

		//Aバスの値を取得
		int i = cpu.getABus().getValue();
		int j = i * 0x2;

		int k = (i & 0x8000 | (j & 0x7FFF));//0x8000は2進数で1000000000000000


		int l = psw_NZ(k);

		  boolean m  = bit(i, 0x4000) != bit(i, 0x8000);
		  boolean n = bit(i,0x4000);

		  if(m) {l |= CPU.PSW_V;}
		  if(n) {l |= CPU.PSW_C;}


		cpu.getRegister(CPU.REG_PSW).setValue(l & 0xFFFF);

		cpu.getSBus().setValue(k & 0xFFFF);
		writeBack(true);
	}
}
