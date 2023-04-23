package entity.instruction

import entity.Simulator
import extensions.funct3
import extensions.mnemonic
import extensions.registerABIName
import extensions.rs1
import extensions.rs2
import extensions.storeImmediate
import extensions.toBinaryString

class StoreInstruction(
    private val type: StoreInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rs2.registerABIName}, $immediate(${rs1.registerABIName})"

    constructor(rawInstruction: Int) : this(
        type = StoreInstructionType.fromFunct3(rawInstruction.funct3()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        immediate = rawInstruction.storeImmediate()
    )

    override fun execute(simulator: Simulator) {
        val base = simulator.readRegister(rs1)
        val value = simulator.readRegister(rs2)
        val address = base + immediate

        when (type) {
            StoreInstructionType.SB -> simulator.storeByte(address, value.toByte())
            StoreInstructionType.SH -> simulator.storeHalf(address, value)
            StoreInstructionType.SW -> simulator.storeWord(address, value)
        }

        simulator.incrementPC()
    }

}

enum class StoreInstructionType {
    SB,
    SH,
    SW;

    companion object {
        fun fromFunct3(funct3: Int) = when (funct3) {
            0b000 -> SB
            0b001 -> SH
            0b010 -> SW
            else -> throw IllegalArgumentException(
                "Unknown funct3 for store instruction: ${funct3.toBinaryString()}"
            )
        }
    }
}