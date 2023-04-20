package entity.instruction

import entity.Processor
import extensions.bits
import extensions.firstByte
import extensions.firstByteUnsigned
import extensions.firstTwoBytes
import extensions.firstTwoBytesUnsigned
import extensions.mnemonic
import extensions.registerABIName
import extensions.toBinary

data class ImmediateInstruction(
    private val type: ImmediateInstructionType,
    private val immediate: Int,
    private val rs1: Int,
    private val rd: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rd.registerABIName}, ${rs1.registerABIName}, $immediate"

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
            ImmediateInstructionType.LB -> processor.loadWithImmediate(source).firstByte()
            ImmediateInstructionType.LH -> processor.loadWithImmediate(source).firstTwoBytes()
            ImmediateInstructionType.LW -> processor.loadWithImmediate(source)
            ImmediateInstructionType.LBU -> processor.loadWithImmediate(source).firstByteUnsigned()
            ImmediateInstructionType.LHU -> processor.loadWithImmediate(source).firstTwoBytesUnsigned()
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

    private fun Processor.loadWithImmediate(source: Int) = loadWord(source + immediate)

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
        fun fromOpcode(opcode: Int, funct3: Int) = when (funct3) {
            0b000 -> ADDI
            0b010 -> SLTI
            0b100 -> XORI
            0b011 -> SLTIU
            0b110 -> ORI
            0b111 -> ANDI
            else -> throw RuntimeException(
                "Unknown funct3 for immediate instruction: " +
                    "opcode=${opcode.toBinary()}, funct3=${funct3.toBinary()}"
            )
        }
    }
}