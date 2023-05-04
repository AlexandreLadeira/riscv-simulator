package entity.instruction

import entity.Simulator
import extensions.mnemonic
import extensions.registerABIName

class RegisterInstruction(
    private val type: RegisterInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val rd: Int,
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} " +
            "${rd.registerABIName}, ${rs1.registerABIName}, ${rs2.registerABIName}"

    override fun execute(simulator: Simulator) {
        val result = type.calculateResult(
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

    fun calculateResult(first: Int, second: Int): Int =
        when (this) {
            ADD -> first + second
            SUB -> first - second
            SLL -> first shl (second and 0b11111)
            SLT -> if (first < second) 1 else 0
            SLTU -> if (first.toUInt() < second.toUInt()) 1 else 0
            XOR -> first xor second
            SRL -> first ushr (second and 0b11111)
            SRA -> first shr (second and 0b11111)
            OR -> first or second
            AND -> first and second
            MUL -> first * second
            MULH -> ((first.toLong() * second.toLong()) ushr 32).toInt()
            MULHSU -> ((first.toLong() * (second.toUInt().toLong())) ushr 32).toInt()
            MULHU -> ((first.toUInt().toULong() * second.toUInt().toULong()) shr 32).toInt()
            DIV -> if (second != 0) first / second else -1
            DIVU -> if (second != 0) (first.toUInt() / second.toUInt()).toInt() else -1
            REM -> if (second != 0) first % second else first
            REMU -> if (second != 0) (first.toUInt() % second.toUInt()).toInt() else first
        }
}