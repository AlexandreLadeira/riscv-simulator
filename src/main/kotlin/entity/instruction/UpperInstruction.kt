package entity.instruction

import entity.Simulator
import extensions.rd
import extensions.registerABIName
import extensions.upperImmediate

class UpperInstruction(
    private val rd: Int,
    private val immediate: Int
) : Instruction() {

    override val disassembly = "LUI      ${rd.registerABIName}, $immediate"

    override fun execute(simulator: Simulator) {
        simulator.writeToRegister(rd, immediate shl 12)
        simulator.incrementPC()
    }

    constructor(rawInstruction: Int) : this(
        rd = rawInstruction.rd(),
        immediate = rawInstruction.upperImmediate()
    )
}