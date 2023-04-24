package entity.instruction

import entity.Simulator
import extensions.mnemonic
import extensions.registerABIName
import extensions.withLastBitCleared

data class ImmediateInstruction(
    private val type: ImmediateInstructionType,
    private val immediate: Int,
    private val rs1: Int,
    private val rd: Int
) : Instruction() {
    override val disassembly = when (type) {
        ImmediateInstructionType.LB,
        ImmediateInstructionType.LH,
        ImmediateInstructionType.LW,
        ImmediateInstructionType.LBU,
        ImmediateInstructionType.LHU,
        ImmediateInstructionType.JALR ->
            "${type.name.mnemonic} ${rd.registerABIName}, $immediate(${rs1.registerABIName})"

        else -> "${type.name.mnemonic} ${rd.registerABIName}, ${rs1.registerABIName}, $immediate"
    }

    override fun execute(simulator: Simulator) {
        val source = simulator.readRegister(rs1)

        val result = when (type) {
            ImmediateInstructionType.JALR -> (simulator.programCounter + 4).also {
                simulator.setPC((source + immediate).withLastBitCleared())
            }

            ImmediateInstructionType.LB -> simulator.loadByte(source + immediate).toInt()
            ImmediateInstructionType.LH -> simulator.loadHalf(source + immediate).toInt()
            ImmediateInstructionType.LW -> simulator.loadWord(source + immediate)
            ImmediateInstructionType.LBU -> simulator.loadByte(source + immediate).toInt() and 0xFF
            ImmediateInstructionType.LHU -> simulator.loadHalf(source + immediate)
                .toInt() and 0xFFFF

            ImmediateInstructionType.ADDI -> source + immediate
            ImmediateInstructionType.SLTI -> if (source < immediate) 1 else 0
            ImmediateInstructionType.SLTIU -> if (source < immediate.toUInt().toLong()) 1 else 0
            ImmediateInstructionType.XORI -> source xor immediate
            ImmediateInstructionType.ORI -> source or immediate
            ImmediateInstructionType.ANDI -> source and immediate
            ImmediateInstructionType.SLLI -> source shl immediate
            ImmediateInstructionType.SRLI -> source ushr immediate
            ImmediateInstructionType.SRAI -> source shr immediate
        }

        simulator.writeToRegister(rd, result)

        if (type != ImmediateInstructionType.JALR) {
            simulator.incrementPC()
        }
    }
}

enum class ImmediateInstructionType {
    JALR,
    LB,
    LH,
    LW,
    LBU,
    LHU,
    ADDI,
    SLTI,
    SLTIU,
    XORI,
    ORI,
    ANDI,
    SLLI,
    SRLI,
    SRAI;
}