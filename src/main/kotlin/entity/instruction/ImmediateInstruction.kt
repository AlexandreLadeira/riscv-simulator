package entity.instruction

import entity.Processor
import extensions.*

data class ImmediateInstruction(
    private val type: ImmediateInstructionType,
    private val immediate: Int,
    private val rs1: Int,
    private val rd: Int
) : Instruction() {
    override val disassembly =
        "${type.name.padEnd(8, ' ')} ${rd.registerABIName}, ${rs1.registerABIName}, $immediate"

    constructor(opcode: Int, data: Int) : this(
        type = ImmediateInstructionType.fromOpcode(opcode, data.bits(12, 14)),
        immediate = data.bits(20, 31),
        rs1 = data.bits(15, 19),
        rd = data.bits(7, 11)
    )

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
        processor.incrementPC()
    }

    private fun Processor.load(source: Int) = memory.loadWord(source + immediate)

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

    companion object {
        fun fromOpcode(opcode: Int, funct3: Int): ImmediateInstructionType {
            return when (funct3) {
                0b000 -> ADDI
                0b010 -> SLTI
                0b100 -> XORI
                0b011 -> SLTIU
                0b110 -> ORI
                0b111 -> ANDI
                else -> throw RuntimeException("Unknown opcode for immediate instruction: opcode=${opcode.toBinary()}, funct3=${funct3.toBinary()}")
            }
        }
    }
}