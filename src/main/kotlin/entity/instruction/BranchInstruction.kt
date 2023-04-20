package entity.instruction

import entity.Processor
import extensions.registerABIName

class BranchInstruction(
    private val type: BranchInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.padEnd(8, ' ')} ${rs1.registerABIName}, ${rs2.registerABIName}, $immediate"

    override fun execute(processor: Processor) {
        val first = processor.readRegister(rs1)
        val second = processor.readRegister(rs2)

        if (type.shouldBranch(first, second)) {
            processor.addToPC(immediate)
        } else {
            processor.incrementPC()
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
