package entity.instruction

import entity.Processor
import extensions.firstByte
import extensions.firstByteUnsigned
import extensions.firstTwoBytes
import extensions.firstTwoBytesUnsigned
import extensions.funct3
import extensions.immediate
import extensions.mnemonic
import extensions.rd
import extensions.registerABIName
import extensions.rs1
import extensions.toBinaryString
import extensions.withLastBitCleared

data class ImmediateInstruction(
    private val type: ImmediateInstructionType,
    private val immediate: Int,
    private val rs1: Int,
    private val rd: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rd.registerABIName}, ${rs1.registerABIName}, $immediate"

    constructor(opcode: Int, rawInstruction: Int) : this(
        type = ImmediateInstructionType.fromOpcode(opcode, rawInstruction.funct3()),
        immediate = rawInstruction.immediate(),
        rs1 = rawInstruction.rs1(),
        rd = rawInstruction.rd()
    )

    override fun execute(processor: Processor) {
        val source = processor.readRegister(rs1)

        val result = when (type) {
            ImmediateInstructionType.JALR -> (processor.programCounter + 4).also {
                processor.setPC((source + immediate).withLastBitCleared())
            }

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

        if (type != ImmediateInstructionType.JALR) {
            processor.incrementPC()
        }
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
        fun fromOpcode(opcode: Int, funct3: Int) = when (opcode) {
            0b0010011 -> when (funct3) {
                0b000 -> ADDI
                0b010 -> SLTI
                0b100 -> XORI
                0b011 -> SLTIU
                0b110 -> ORI
                0b111 -> ANDI
                else -> unknownFunct3Error(funct3)
            }

            0b0000011 -> when (funct3) {
                0b000 -> LB
                0b001 -> LH
                0b010 -> LW
                0b100 -> LBU
                0b101 -> LHU
                else -> unknownFunct3Error(funct3)
            }

            0b1100111 -> JALR

            else -> throw RuntimeException(
                "Unknown opcode for immediate instruction ${opcode.toBinaryString()}"
            )
        }

        private fun unknownFunct3Error(funct3: Int): Nothing =
            throw IllegalArgumentException(
                "Unknown funct3 for immediate instruction ${funct3.toBinaryString()}"
            )
    }

}