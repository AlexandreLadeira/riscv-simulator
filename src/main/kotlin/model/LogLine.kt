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
        instruction.disassembly.padEnd(32, ' ') +
            " PC=${pc.toHexString().padStart(8, '0')}, " +
            "[${rawInstruction.toHexString().padStart(8, '0')}], " +
            "x$rdIndex=$rdValue, " +
            "x$rs1Index=$rs1Value, " +
            "x$rs2Index=$rs2Value "

}



