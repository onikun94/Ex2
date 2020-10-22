package sep3.model.cycle;


import sep3.Model;
import sep3.model.CPU;
import sep3.model.Decoder;
import sep3.model.Memory;
import sep3.model.operation.InstructionSet;

public class ReadFromOpe1 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("%% IF1 %%");
		// ステータスカウンタの設定。次の２行は、すべての状態において、最初に必ず記述すること
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_FF1);

		int addressing = cpu.getDecoder().getFromMode();

		System.out.println("F1ステップを実行してます");

			// MAR をアドレスバスに流す
			model.getAddrBusSelector().selectFrom(CPU.REG_MAR);
			// メモリを読み出してデータバスへ出力
			model.getMemory().access(Memory.MEM_RD);
			// データバスの値をMDRへ送る
			model.getDataBusSelector().selectTo(CPU.REG_MDR);


			//ポストインクリメントレジスタ間接アドレシングの場合インクリメント
			if(addressing == Decoder.MODE_IP) {
				System.out.println("F1ステップにてインクリメントしてます");
				int register = cpu.getDecoder().getFromRegister();
		        //指定されたレジスタからAバスに流す
				cpu.getABusSelector().selectFrom(register);
				//Bバスは閉じる
				cpu.getBBusSelector().selectFrom();
				//ALでインクリメントしSバスに流す
		        cpu.getALU().operate(InstructionSet.OP_INC );
				//Sバスから指定レジスタに送る
		        cpu.getSBusSelector().selectTo(register);
			}


		// 次の状態へ
		//return cpu.getStateFactory().getState(StateFactory.SC_HLT);
		return cpu.getStateFactory().getState(StateFactory.SC_FF2);
	}
}