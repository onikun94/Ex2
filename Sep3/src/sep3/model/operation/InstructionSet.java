package sep3.model.operation;

import sep3.misc.Factory;
import sep3.model.CPU;

// SEP-3 の命令セット
public class InstructionSet extends Factory<Integer, Operation> {
	// 値は、操作コードのビット列に従う
	public static final int OP_HLT  = 0x00;		//完了
	public static final int OP_CLR  = 0x04;     //完了1
	public static final int OP_ASL  = 0x08;     //完了2
	public static final int OP_ASR  = 0x09;     //完了3
	public static final int OP_LSL  = 0x0C;     //完了4
	public static final int OP_LSR  = 0x0D;     //完了5
	public static final int OP_ROL  = 0x0E;     //完了21
	public static final int OP_ROR  = 0x0F;     //完了22
	public static final int OP_MOV  = 0x10;		//完了
	public static final int OP_JMP  = 0x11;		//完了
	public static final int OP_RET  = 0x12;		//完了24(JSR)
	public static final int OP_RIT  = 0x13;		//完了25(JSR)
	public static final int OP_ADD  = 0x14;		//完了
	public static final int OP_RJP  = 0x15;     //完了6
	public static final int OP_SUB  = 0x18;		//完了
	public static final int OP_CMP  = 0x1B;     //完了7
	public static final int OP_NOP  = 0x1C;		//完了8
	public static final int OP_OR   = 0x20;     //完了9
	public static final int OP_XOR  = 0x21;     //完了10
	public static final int OP_AND  = 0x22;     //完了11
	public static final int OP_BIT  = 0x23;     //完了12
	public static final int OP_JSR  = 0x2C;     //完了23
	public static final int OP_RJS  = 0x2D;     //完了27
	public static final int OP_SVC  = 0x2E;		//完了26(JSR)
	public static final int OP_BRN  = 0x30;     //完了13
	public static final int OP_BRZ  = 0x31;     //完了14
	public static final int OP_BRV  = 0x32;     //完了15
	public static final int OP_BRC  = 0x33;     //完了16
	public static final int OP_RBN  = 0x38;     //完了17
	public static final int OP_RBZ  = 0x39;     //完了18
	public static final int OP_RBV  = 0x3A;     //完了19
	public static final int OP_RBC  = 0x3B;     //完了20
	public static final int OP_INC  = 0x100;	//完了		// 命令とは直接関係がなく、内部でだけ使う＋１動作
	public static final int OP_DEC  = 0x101;	//完了		// 命令とは直接関係がなく、内部でだけ使う－１動作
	public static final int OP_THRA = 0x102;	//完了		// 命令とは直接関係がなく、Aバスのデータを素通しする動作
	public static final int OP_THRB = 0x103;	//完了		// 命令とは直接関係がなく、Bバスのデータを素通しする動作
	public static final int OP_ADD2 = 0x104;	//完了		// ADDと同じだが、PSW更新せず
	public static final int OP_ILL  = 0x200;	//完了		// 不正な命令

	private Operation illop;

	public InstructionSet(CPU cpu) {
		makeItem(Integer.valueOf(OP_HLT),  new HltOperation(cpu));
		makeItem(Integer.valueOf(OP_CLR),  new ClrOperation(cpu));
		makeItem(Integer.valueOf(OP_ASL),  new AslOperation(cpu));
		makeItem(Integer.valueOf(OP_ASR),  new AsrOperation(cpu));
		makeItem(Integer.valueOf(OP_LSL),  new LslOperation(cpu));
		makeItem(Integer.valueOf(OP_LSR),  new LsrOperation(cpu));
		makeItem(Integer.valueOf(OP_ROL),  new RolOperation(cpu));
		makeItem(Integer.valueOf(OP_ROR),  new RorOperation(cpu));
		makeItem(Integer.valueOf(OP_MOV),  new MovOperation(cpu));
		makeItem(Integer.valueOf(OP_JMP),  new JmpOperation(cpu));
		makeItem(Integer.valueOf(OP_RET),  new RetOperation(cpu));
		makeItem(Integer.valueOf(OP_RIT),  new RitOperation(cpu));
		makeItem(Integer.valueOf(OP_ADD),  new AddOperation(cpu));
		makeItem(Integer.valueOf(OP_RJP),  new RjpOperation(cpu));
		makeItem(Integer.valueOf(OP_SUB),  new SubOperation(cpu));
		makeItem(Integer.valueOf(OP_CMP),  new CmpOperation(cpu));
		makeItem(Integer.valueOf(OP_NOP),  new NopOperation(cpu));
		makeItem(Integer.valueOf(OP_OR),   new OrOperation(cpu));
		makeItem(Integer.valueOf(OP_XOR),  new XorOperation(cpu));
		makeItem(Integer.valueOf(OP_AND),  new AndOperation(cpu));
		makeItem(Integer.valueOf(OP_BIT),  new BitOperation(cpu));
		makeItem(Integer.valueOf(OP_JSR),  new JsrOperation(cpu));
		makeItem(Integer.valueOf(OP_RJS),  new RjsOperation(cpu));
		makeItem(Integer.valueOf(OP_SVC),  new SvcOperation(cpu));
		makeItem(Integer.valueOf(OP_BRN),  new BrnOperation(cpu));
		makeItem(Integer.valueOf(OP_BRZ),  new BrzOperation(cpu));
		makeItem(Integer.valueOf(OP_BRV),  new BrvOperation(cpu));
		makeItem(Integer.valueOf(OP_BRC),  new BrcOperation(cpu));
		makeItem(Integer.valueOf(OP_RBN),  new RbnOperation(cpu));
		makeItem(Integer.valueOf(OP_RBZ),  new RbzOperation(cpu));
		makeItem(Integer.valueOf(OP_RBV),  new RbvOperation(cpu));
		makeItem(Integer.valueOf(OP_RBC),  new RbcOperation(cpu));
		makeItem(Integer.valueOf(OP_INC),  new IncOperation(cpu));
		makeItem(Integer.valueOf(OP_DEC),  new DecOperation(cpu));
		makeItem(Integer.valueOf(OP_THRA), new ThraOperation(cpu));
		makeItem(Integer.valueOf(OP_THRB), new ThrbOperation(cpu));
		makeItem(Integer.valueOf(OP_ADD2), new Add2Operation(cpu));
		illop = new IllOperation(cpu);
		makeItem(Integer.valueOf(OP_ILL),  illop);
	}

	// 用意されている命令であるかどうかのチェック（不正命令のチェック）
	public boolean isLegalInstruction(int opcode) {
		Operation v;
		switch (opcode) {
		case OP_INC: case OP_DEC: case OP_THRA: case OP_THRB: case OP_ADD2: case OP_ILL:
			v = null;
			break;
		default:
			//v = getItem(new Integer(opcode));
			v = getItem(Integer.valueOf(opcode));
			break;
		}
		return v != null;
	}
	// 操作コードを指定して、命令クラスを取得する
	public Operation getOperation(int opcode) {
		Operation v;
		//v = getItem(new Integer(opcode));
		v = getItem(Integer.valueOf(opcode));
		if (v == null) {
			v = illop;
		}
		return v;
	}
}
