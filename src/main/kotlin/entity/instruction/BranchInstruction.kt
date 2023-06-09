package entity.instruction

import entity.Simulator
import extensions.mnemonic
import extensions.registerABIName

class BranchInstruction(
    private val type: BranchInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rs1.registerABIName}, ${rs2.registerABIName}, $immediate"

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
}
