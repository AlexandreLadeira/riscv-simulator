package entity.instruction

import entity.Simulator

class BreakInstruction : Instruction() {
    override val disassembly = "EBREAK  "

    override fun execute(simulator: Simulator) {
        simulator.stop()
    }
}