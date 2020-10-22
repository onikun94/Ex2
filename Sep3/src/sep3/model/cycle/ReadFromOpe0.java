package sep3.model.cycle;

import sep3.Model;
import sep3.model.CPU;
import sep3.model.Decoder;
import sep3.model.operation.InstructionSet;

public class ReadFromOpe0 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("%% IF0 %%");
		// ステータスカウンタの設定。次の２行は、すべての状態において、最初に必ず記述すること
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_FF0);

		int addressing = cpu.getDecoder().getFromMode();

		if(addressing == Decoder.MODE_D ) {  //直接アドレシング
			System.out.println("F0ステップにて直接アドレシングを使用しました");

			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getFromRegister());
			//Bバスは閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA);
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

			return cpu.getStateFactory().getState(StateFactory.SC_FF2);

		}else if(addressing == Decoder.MODE_I) { //間接アドレシング
			System.out.println("F0ステップにて間接アドレシングを使用しました");

			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getFromRegister());
			//Bバスは閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA);
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

		}else if(addressing == Decoder.MODE_MI) { //プレデクリメントレジスタ間接アドレシング
			System.out.println("F0ステップにてプレデクリメントレジスタ間接アドレシングを使用しました");
			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getFromRegister());
			//Bバスを閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUでデクリメントしてSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_DEC);
	        //Sバスから指定レジスタに送る
	        cpu.getSBusSelector().selectTo(cpu.getDecoder().getFromRegister());
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

		}else if(addressing == Decoder.MODE_IP) { //ポストインクリメントレジスタ間接アドレシング
			System.out.println("F0ステップにてポストインクリメントレジスタ間接アドレシングを使用しました");
			//指定されたレジスタからAバスに流す
			cpu.getABusSelector().selectFrom(cpu.getDecoder().getFromRegister());
			//Bバスを閉じる
			cpu.getBBusSelector().selectFrom();
			//ALUをそのまま貫通しSバスに流す
	        cpu.getALU().operate(InstructionSet.OP_THRA );
			//SバスからMARに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MAR);
			//SバスからMDRに送る
	        cpu.getSBusSelector().selectTo(CPU.REG_MDR);

		}

		// 次の状態へ
		return cpu.getStateFactory().getState(StateFactory.SC_FF1);
	}

}
