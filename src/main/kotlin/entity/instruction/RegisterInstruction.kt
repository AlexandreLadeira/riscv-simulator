package entity.instruction

import entity.Simulator
import extensions.funct3
import extensions.funct7
import extensions.mnemonic
import extensions.rd
import extensions.registerABIName
import extensions.rs1
import extensions.rs2
import extensions.toBinaryString

class RegisterInstruction(
    private val type: RegisterInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val rd: Int,
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} " +
            "${rd.registerABIName}, ${rs1.registerABIName}, ${rs2.registerABIName}"

    constructor(rawInstruction: Int) : this(
        type = RegisterInstructionType.fromFunct3(rawInstruction.funct3(), rawInstruction.funct7()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        rd = rawInstruction.rd()
    )

    override fun execute(simulator: Simulator) {
        val result = type.getResult(
            first = simulator.readRegister(rs1),
            second = simulator.readRegister(rs2)
        )

        simulator.writeToRegister(rd, result)
        simulator.incrementPC()
    }
}

enum class RegisterInstructionType {
    ADD,
    SUB,
    SLL,
    SLT,
    SLTU,
    XOR,
    SRL,
    SRA,
    OR,
    AND,
    MUL,
    MULH,
    MULHSU,
    MULHU,
    DIV,
    DIVU,
    REM,
    REMU;

    fun getResult(first: Int, second: Int): Int = when (this) {
        ADD -> first + second
        SUB -> first - second
        SLL -> first shl (second and 0x11111)
        SLT -> if (first < second) 1 else 0
        SLTU -> if (first.toUInt() < second.toUInt()) 1 else 0
        XOR -> first xor second
        SRL -> first ushr (second and 0x11111)
        SRA -> first shr (second and 0x11111)
        OR -> first or second
        AND -> first and second
        MUL -> first * second
        MULH -> ((first.toLong() * second.toLong()) ushr 32).toInt()
        MULHSU -> ((first.toLong() * (second.toUInt().toLong())) ushr 32).toInt()
        MULHU -> ((first.toULong() * second.toULong()) shr 32).toInt()
        DIV -> if (second != 0) first / second else -1
        DIVU -> if (second != 0) (first.toUInt() / second.toUInt()).toInt() else -1
        REM -> if (second != 0) first % second else first
        REMU -> if (second != 0) (first.toUInt() % second.toUInt()).toInt() else first
    }

    companion object {
        fun fromFunct3(funct3: Int, funct7: Int) = when (funct7) {
            0 -> when (funct3) {
                0b000 -> ADD
                0b001 -> SLL
                0b010 -> SLT
                0b011 -> SLTU
                0b100 -> XOR
                0b101 -> SRL
                0b110 -> OR
                0b111 -> AND
                else -> throw IllegalArgumentException(
                    "Unknown funct3 for register instruction: ${funct3.toBinaryString()}"
                )
            }

            1 -> when (funct3) {
                0b000 -> MUL
                0b001 -> MULH
                0b010 -> MULHSU
                0b011 -> MULHU
                0b100 -> DIV
                0b101 -> DIVU
                0b110 -> REM
                0b111 -> REMU
                else -> throw IllegalArgumentException(
                    "Unknown funct3 for register instruction: ${funct3.toBinaryString()}"
                )
            }

            0b0100000 -> when (funct3) {
                0b000 -> SUB
                0b101 -> SRA
                else -> throw IllegalArgumentException(
                    "Unknown funct3 for register instruction: ${funct3.toBinaryString()}"
                )
            }

            else -> throw IllegalArgumentException(
                "Unknown funct7 for register instruction: ${funct7.toBinaryString()}"
            )
        }
    }
}