package sep3.model.cycle;

import sep3.Model;
import sep3.model.CPU;
import sep3.model.Decoder;

public class ExecState0 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("State EX0");
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_EX0);

		System.out.println("E0ステップにて演算命令を実行してます");
		// ALUで、命令ごとの演算をする。
		int opCode = cpu.getDecoder().getOpCode();
		cpu.getALU().operate(opCode);

		// 次の状態へ
		int nextState;
		if (cpu.getDecoder().getToMode() == Decoder.MODE_D) {
			System.out.println("E0ステップにて直接アドレシングのためこの命令は終了です");
			System.out.println("～～～～～～～～～～～～～～～～～～～～～～～～～～～");
			nextState = StateFactory.SC_IF0;
		} else {
			System.out.println("E1ステップに移行します");
			nextState = StateFactory.SC_EX1;
		}
		return cpu.getStateFactory().getState(nextState);
	}
}
