package sep3.model.cycle;

import sep3.Model;
import sep3.model.CPU;
import sep3.model.Memory;
import sep3.model.operation.InstructionSet;

public class ExecState1 extends State {
	@Override
	public State clockstep(Model model) {
		//System.out.println("%% IF0 %%");
		// ステータスカウンタの設定。次の２行は、すべての状態において、最初に必ず記述すること
		CPU cpu = model.getCPU();
		cpu.getRegister(CPU.REG_SC).setInitValue(StateFactory.SC_EX1);

		System.out.println("E1ステップにてメモリに結果を書き戻しています");
		//MARにTオペランドのアドレスをセットしたまま

		model.getDataBusSelector().selectFrom(CPU.REG_MDR);

		model.getMemory().access(Memory.MEM_WR);


        //F→R7の実装

        if(cpu.getDecoder().getOpCode() == 0x2C) { // JSR命令かどうか
        	System.out.println("E1ステップにてJSRの追加処理を行っています");
            //F→R7すなわちB0→R7を行う

        	//Aバスは閉じる
            cpu.getABusSelector().selectFrom();
            //BバスにB0のデータを流す
            cpu.getBBusSelector().selectFrom(CPU.REG_B0);
            //ALUはBバスの値をそのままSバスに流す
            cpu.getALU().operate(InstructionSet.OP_THRB);
            // Sバスの値をPC(R7)に送る
            cpu.getSBusSelector().selectTo(CPU.REG_R7);

        }else if(cpu.getDecoder().getOpCode() == 0x2D) { //RJS命令かどうか
        	System.out.println("E1ステップにてRJSの追加処理を行っています");
        	//相対参照時に使うR7の値を持ってくる
        	//cpu.getABusSelector().selectFrom(CPU.REG_MDR);
        	cpu.getABusSelector().selectFrom(CPU.REG_R7);
            //BバスにB0のデータを流す
            cpu.getBBusSelector().selectFrom(CPU.REG_B0);

            //相対参照値のため加算する
    		int i = cpu.getABus().getValue();
    		int j = cpu.getBBus().getValue();
    		System.out.println("i=");
    		System.out.println(i);
    		System.out.println("j=");
    		System.out.println(j);
    		int o = i + j;
    		//Sバスに送りPCに送る
    		cpu.getSBus().setValue(o & 0xFFFF);
    		cpu.getSBusSelector().selectTo(CPU.REG_R7);

            //ALUはBバスの値をそのままSバスに流す
            cpu.getALU().operate(InstructionSet.OP_THRB);
            // Sバスの値をPC(R7)に送る
            cpu.getSBusSelector().selectTo(CPU.REG_R7);

        }

        System.out.println("～～～～～～～～～～～～～～～～～～～～～～～～～～～");

		// 次の状態へ
		return cpu.getStateFactory().getState(StateFactory.SC_IF0);
	}

}
