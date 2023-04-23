package model

import entity.instruction.Instruction
import extensions.toHexString

data class LogLine(
    val pc: Int,
    val rawInstruction: Int,
    val rdIndex: Int,
    val rs1Index: Int,
    val rs2Index: Int,
    val rdValue: Int,
    val rs1Value: Int,
    val rs2Value: Int,
    val instruction: Instruction
) {
    override fun toString(): String =
        "PC=${pc.toHexString().padStart(8, '0')}, " +
            "[${rawInstruction.toHexString().padStart(8, '0')}], " +
            "x${rdIndex.toString().padStart(2, '0')}=${rdValue.toHexString().padStart(8, '0')}, " +
            "x${rs1Index.toString().padStart(2, '0')}=${rs1Value.toHexString().padStart(8, '0')}, " +
            "x${rs2Index.toString().padStart(2, '0')}=${rs2Value.toHexString().padStart(8, '0')} " +
            instruction.disassembly

}



