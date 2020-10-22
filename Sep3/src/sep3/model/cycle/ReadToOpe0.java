package sep3.model.cycle;

import sep3.Model;
import sep3.model.CPU;
import sep3.model.Decoder;
import sep3.model.operation.InstructionSet;

public class ReadToOpe0 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("%% IF0 %%");
		// ステータスカウンタの設定。次の２行は、すべての状態において、最初に必ず記述すること
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_TF0);

		int addressing = cpu.getDecoder().getToMode();

		if(addressing == Decoder.MODE_D ) {  //直接アドレシング

			System.out.println("T0ステップにて直接アドレシングを実行してます");
			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getToRegister());
			//Bバスは閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA);
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

	     // 次の状態へ
			return cpu.getStateFactory().getState(StateFactory.SC_EX0);

		}else if(addressing == Decoder.MODE_I) { //間接アドレシング
			System.out.println("T0ステップにてア間接ドレシングを実行してます");

			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getToRegister());
			//Bバスは閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA);
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

		}else if(addressing == Decoder.MODE_IP) { //ポストインクリメントレジスタ間接アドレシング

			System.out.println("T0ステップにてポストインクリメントレジスタ間接アドレシングを実行してます");
			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getToRegister());
			//Bバスは閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA );

			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

		}
		// 次の状態へ
		return cpu.getStateFactory().getState(StateFactory.SC_TF1);
	}

}
