package sep3.model.operation;
import sep3.model.CPU;

public class LsrOperation extends Operation {
	private CPU cpu;
	LsrOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {
		// Bバスは使わず、MDRの値をAバスへ出力する
		useABus(true);
		useBBus(false);

		//Aバスの値を取得
		int i = cpu.getABus().getValue();
		int j = i / 0x2;

		int k = (j & 0xFFFF ); //最上位ビットを0に

		//0x8000は2進数で1000 0000 0000 0000
		//0x4000は2進数で0100 0000 0000 0000
		//0x7FFF 0111 1111 1111 1111
		//0xFFFF 1111 1111 1111 1111


		int l = psw_NZ(k);

		  //boolean m  = bit(i, 0x4000) != bit(i, 0x8000); //0x4000は2進数で0100 0000 0000 0000 上位2bitのうちどちらか0どちらか１
		  boolean n = bit(i,0x0001); //最下位ビットが1ならキャリーする

		  //if(m) {l |= CPU.PSW_V;}
		  if(n) {l |= CPU.PSW_C;}
			// C will not change
		cpu.getRegister(CPU.REG_PSW).setValue(l & 0xFFFF);

		// Sバスの値をToオペランドに書き込む
		cpu.getSBus().setValue(k & 0xFFFF);
		writeBack(true);
	}
}
