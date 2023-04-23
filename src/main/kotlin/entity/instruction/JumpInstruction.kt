package entity.instruction

import entity.Simulator
import extensions.jumpImmediate
import extensions.rd
import extensions.registerABIName

class JumpInstruction(
    private val rd: Int,
    private val immediate: Int
) : Instruction() {

    override val disassembly = "jal      ${rd.registerABIName}, $immediate"

    constructor(rawInstruction: Int) : this(
        rd = rawInstruction.rd(),
        immediate = rawInstruction.jumpImmediate()
    )

    override fun execute(simulator: Simulator) {
        simulator.writeToRegister(rd, simulator.programCounter + 4)
        simulator.addToPC(immediate)
    }
}