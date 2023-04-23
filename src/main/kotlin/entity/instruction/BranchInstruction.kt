package entity.instruction

import entity.Simulator
import extensions.branchImmediate
import extensions.funct3
import extensions.mnemonic
import extensions.registerABIName
import extensions.rs1
import extensions.rs2
import extensions.toBinaryString

class BranchInstruction(
    private val type: BranchInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rs1.registerABIName}, ${rs2.registerABIName}, $immediate"

    constructor(rawInstruction: Int) : this(
        type = BranchInstructionType.fromFunct3(rawInstruction.funct3()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        immediate = rawInstruction.branchImmediate()
    )

    override fun execute(simulator: Simulator) {
        val first = simulator.readRegister(rs1)
        val second = simulator.readRegister(rs2)

        if (type.shouldBranch(first, second)) {
            simulator.addToPC(immediate)
        } else {
            simulator.incrementPC()
        }
    }

}

enum class BranchInstructionType {
    BEQ,
    BNE,
    BLT,
    BGE,
    BLTU,
    BGEU;

    fun shouldBranch(first: Int, second: Int) = when (this) {
        BEQ -> first == second
        BNE -> first != second
        BLT -> first < second
        BGE -> first >= second
        BLTU -> first.toUInt() < second.toUInt()
        BGEU -> first.toUInt() >= second.toUInt()
    }

    companion object {
        fun fromFunct3(funct3: Int) = when (funct3) {
            0b000 -> BEQ
            0b001 -> BNE
            0b100 -> BLT
            0b101 -> BGE
            0b110 -> BLTU
            0b111 -> BGEU
            else -> throw IllegalArgumentException(
                "Unknown funct3 for branch instruction: ${funct3.toBinaryString()}"
            )
        }
    }
}
