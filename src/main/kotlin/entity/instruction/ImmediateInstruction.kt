package entity.instruction

import entity.Simulator
import extensions.funct3
import extensions.funct7
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
        type = ImmediateInstructionType.fromOpcode(opcode, rawInstruction.funct3(), rawInstruction.funct7()),
        immediate = rawInstruction.immediate(),
        rs1 = rawInstruction.rs1(),
        rd = rawInstruction.rd()
    )

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
            ImmediateInstructionType.LHU -> simulator.loadHalf(source + immediate).toInt() and 0xFFFF
            ImmediateInstructionType.ADDI -> source + immediate
            ImmediateInstructionType.SLTI -> if (source < immediate) 1 else 0
            ImmediateInstructionType.SLTIU -> if (source < immediate.toUInt().toLong()) 1 else 0
            ImmediateInstructionType.XORI -> source xor immediate
            ImmediateInstructionType.ORI -> source or immediate
            ImmediateInstructionType.ANDI -> source and immediate
            ImmediateInstructionType.SLLI -> source shl (immediate and 0b11111)
            ImmediateInstructionType.SRLI -> source ushr (immediate and 0b11111)
            ImmediateInstructionType.SRAI -> source shr (immediate and 0b11111)
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


    companion object {
        fun fromOpcode(opcode: Int, funct3: Int, funct7: Int) = when (opcode) {
            0b0010011 -> when (funct3) {
                0b000 -> ADDI
                0b010 -> SLTI
                0b100 -> XORI
                0b011 -> SLTIU
                0b110 -> ORI
                0b111 -> ANDI
                0b001 -> SLLI
                0b101 -> if (funct7 == 0) SRLI else SRAI
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