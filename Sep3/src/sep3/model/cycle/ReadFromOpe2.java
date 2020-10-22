package sep3.model.cycle;

import sep3.Model;
import sep3.model.CPU;
import sep3.model.operation.InstructionSet;

public class ReadFromOpe2 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("%% IF0 %%");
		// ステータスカウンタの設定。次の２行は、すべての状態において、最初に必ず記述すること
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_FF2);

		System.out.println("F2ステップを実行してます");

		//MDRからAバスへ送る
		cpu.getABusSelector().selectFrom(CPU.REG_MDR);
		//ALUはAバスの値をそのままSバスへ流す
		cpu.getALU().operate(InstructionSet.OP_THRA);

		//Sバスの値をB0へ送る
		cpu.getSBusSelector().selectTo(CPU.REG_B0);
		//Bバスを閉じる
		cpu.getBBusSelector().selectFrom();



		// 次の状態へ
		return cpu.getStateFactory().getState(StateFactory.SC_TF0);
	}

}
