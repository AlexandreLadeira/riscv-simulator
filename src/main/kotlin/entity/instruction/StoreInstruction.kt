package entity.instruction

import entity.Processor
import extensions.bits
import extensions.mnemonic
import extensions.registerABIName
import extensions.toBinary

class StoreInstruction(
    private val type: StoreInstructionType,
    private val rs1: Int,
    private val rs2: Int,
    private val immediate: Int
) : Instruction() {
    override val disassembly =
        "${type.name.mnemonic} ${rs2.registerABIName}, $immediate(${rs1.registerABIName})"

    constructor(opcode: Int, data: Int) : this(
        type = StoreInstructionType.fromOpcode(opcode, data.bits(12, 14)),
        rs1 = data.bits(15, 19),
        rs2 = data.bits(20, 24),
        immediate = (data.bits(25, 31) shl 5) or data.bits(7, 11)
    )

    override fun execute(processor: Processor) {
        val base = processor.readRegister(rs1)
        val value = processor.readRegister(rs2)
        val address = base + immediate

        when (type) {
            StoreInstructionType.SB -> processor.storeByte(address, value.toByte())
            StoreInstructionType.SH -> TODO()
            StoreInstructionType.SW -> processor.storeWord(address, value)
        }

        processor.incrementPC()
    }

}

enum class StoreInstructionType {
    SB,
    SH,
    SW;

    companion object {
        fun fromOpcode(opcode: Int, funct3: Int) = when (funct3) {
            0b000 -> SB
            0b001 -> SH
            0b010 -> SW
            else -> throw IllegalArgumentException(
                "Unknown opcode for store instruction: opcode=${opcode.toBinary()}, funct3=${funct3.toBinary()}"
            )
        }
    }
}