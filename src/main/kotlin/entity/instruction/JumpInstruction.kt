package entity.instruction

import entity.Processor
import extensions.jumpImmediate
import extensions.rd

class JumpInstruction(
    private val rd: Int,
    private val immediate: Int
) : Instruction() {

    override val disassembly = "j        $immediate"

    constructor(rawInstruction: Int) : this(
        rd = rawInstruction.rd(),
        immediate = rawInstruction.jumpImmediate()
    )

    override fun execute(processor: Processor) {
        processor.writeToRegister(rd, processor.programCounter + 4)
        processor.addToPC(immediate)
    }
}