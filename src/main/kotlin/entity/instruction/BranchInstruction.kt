package entity.instruction

import entity.Processor

class BranchInstruction(
    private val type: BranchInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val mnemonic = type.toString()
    override fun execute(processor: Processor) {
        val first = processor.readRegister(rs1)
        val second = processor.readRegister(rs2)

        if (type.shouldBranch(first, second)) {
            processor.programCounter += immediate
        } else {
            processor.incrementProgramCounter()
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
