package entity.instruction

import entity.Simulator
import extensions.mnemonic
import extensions.registerABIName

class UpperInstruction(
    private val type: UpperInstructionType,
    private val rd: Int,
    private val immediate: Int
) : Instruction() {

    override val disassembly = "${type.name.mnemonic} ${rd.registerABIName}, $immediate"

    override fun execute(simulator: Simulator) {
        val upperImmediate = immediate shl 12

        when (type) {
            UpperInstructionType.AUIPC -> simulator.writeToRegister(rd, simulator.programCounter + upperImmediate)
            UpperInstructionType.LUI -> simulator.writeToRegister(rd, upperImmediate)
        }

        simulator.incrementPC()
    }
}

enum class UpperInstructionType {
    LUI,
    AUIPC;
}