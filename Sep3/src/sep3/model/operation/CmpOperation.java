package sep3.model.operation;
import sep3.model.CPU;

// 通常のADD命令用
public class CmpOperation extends Operation {
	private CPU cpu;
	CmpOperation(CPU cpu) { super(cpu); this.cpu = cpu; }
	public void operate() {
		System.out.println("EX0にてCMP命令が呼び出されました");
		// AバスにMDRの値を、BバスへB0の値を出力する
		useABus(true);
		useBBus(true);

		// 両バスの値を足してSバスに渡す
		int i = cpu.getABus().getValue();
		int j = cpu.getBBus().getValue();
		int o = i - j;
        System.out.print(o);
		// PSWの更新
		int p = psw_NZ(o);

		// オーバーフローするのは、iとjの符号ビットが同じで、oの符号ビットと異なる場合
		boolean diffMSBin  = bit(i, 0x8000) != bit(j, 0x8000);
		boolean diffMSBout = bit(j, 0x8000) != bit(o, 0x8000);
		if (diffMSBin && diffMSBout)			{ p |= CPU.PSW_V; }
		if (bit(o, 0x10000))					{ p |= CPU.PSW_C; }
		cpu.getRegister(CPU.REG_PSW).setValue(p);

		// キャリーがあったら捨てて、Sバスの値をToオペランドに書き込む
		//cpu.getSBus().setValue(o & 0xFFFF);
		//writeBack(true);
	}
}
