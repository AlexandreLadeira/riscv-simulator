package entity.instruction

import entity.Processor
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

    override fun execute(processor: Processor) {
        val result = type.getResult(
            first = processor.readRegister(rs1),
            second = processor.readRegister(rs2)
        )

        processor.writeToRegister(rd, result)
        processor.incrementPC()
    }
}

enum class RegisterInstructionType {
    SLLI,
    SRLI,
    SRAI,
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
        SLLI -> TODO()
        SRLI -> TODO()
        SRAI -> TODO()
        ADD -> first + second
        SUB -> first - second
        SLL -> TODO()
        SLT -> TODO()
        SLTU -> TODO()
        XOR -> first xor second
        SRL -> TODO()
        SRA -> TODO()
        OR -> first or second
        AND -> first and second
        MUL -> first * second
        MULH -> TODO()
        MULHSU -> TODO()
        MULHU -> TODO()
        DIV -> first / second
        DIVU -> TODO()
        REM -> first % second
        REMU -> TODO()
    }
}