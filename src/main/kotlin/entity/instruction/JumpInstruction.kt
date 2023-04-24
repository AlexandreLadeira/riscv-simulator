package entity.instruction

import entity.Simulator
import extensions.registerABIName

class JumpInstruction(
    private val rd: Int,
    private val immediate: Int
) : Instruction() {

    override val disassembly = "JAL      ${rd.registerABIName}, $immediate"

    override fun execute(simulator: Simulator) {
        simulator.writeToRegister(rd, simulator.programCounter + 4)
        simulator.addToPC(immediate)
    }
}