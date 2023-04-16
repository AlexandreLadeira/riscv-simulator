package entity.instruction

import entity.Processor
import extensions.firstByte
import extensions.firstByteUnsigned
import extensions.firstTwoBytes
import extensions.firstTwoBytesUnsigned

class ImmediateInstruction(
    private val type: ImmediateInstructionType,
    private val immediate: Int,
    private val rs1: Int,
    private val rd: Int
) : Instruction() {
    override val mnemonic = type.toString()
    override fun execute(processor: Processor) {
        val source = processor.readRegister(rs1)

        val result = when (type) {
            ImmediateInstructionType.JALR -> TODO()
            ImmediateInstructionType.LB -> processor.load(source).firstByte()
            ImmediateInstructionType.LH -> processor.load(source).firstTwoBytes()
            ImmediateInstructionType.LW -> processor.load(source)
            ImmediateInstructionType.LBU -> processor.load(source).firstByteUnsigned()
            ImmediateInstructionType.LHU -> processor.load(source).firstTwoBytesUnsigned()
            ImmediateInstructionType.ADDI -> source + immediate
            ImmediateInstructionType.SLTI -> if (source < immediate) 1 else 0
            ImmediateInstructionType.SLTIU -> TODO()
            ImmediateInstructionType.XORI -> source xor immediate
            ImmediateInstructionType.ORI -> source or immediate
            ImmediateInstructionType.ANDI -> source and immediate
        }

        processor.writeToRegister(rd, result)
        processor.incrementProgramCounter()
    }

    private fun Processor.load(source: Int) = memory.load(source + immediate)

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
    ANDI;
}