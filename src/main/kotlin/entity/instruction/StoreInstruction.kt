package entity.instruction

import entity.Simulator
import extensions.mnemonic
import extensions.registerABIName

class StoreInstruction(
    private val type: StoreInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rs2.registerABIName}, $immediate(${rs1.registerABIName})"

    override fun execute(simulator: Simulator) {
        val base = simulator.readRegister(rs1)
        val value = simulator.readRegister(rs2)
        val address = base + immediate

        when (type) {
            StoreInstructionType.SB -> simulator.storeByte(address, value.toByte())
            StoreInstructionType.SH -> simulator.storeHalf(address, value.toShort())
            StoreInstructionType.SW -> simulator.storeWord(address, value)
        }

        simulator.incrementPC()
    }

}

enum class StoreInstructionType {
    SB,
    SH,
    SW;
}