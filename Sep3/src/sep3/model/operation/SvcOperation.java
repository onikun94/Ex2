package sep3.model.operation;
import sep3.model.CPU;

public class SvcOperation extends Operation {
	private CPU cpu;
	SvcOperation(CPU cpu) { super(cpu); this.cpu = cpu; }

	public void operate() {
		 // AバスへMDR、BバスへB0の値を出力する
        useABus(true);
        useBBus(true);

        //R7をMDRへセット

        // レジスタ7の値をAバスに流す
        cpu.getABusSelector().selectFrom(CPU.REG_R7);
        //Bバスは閉じる
        cpu.getBBusSelector().selectFrom();
        // ALUはAバスの値をそのままSバスに流す
        cpu.getALU().operate(InstructionSet.OP_THRA);
        // Sバスの値をMDRへ送る
        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

	}
}
