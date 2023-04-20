package model

import entity.instruction.Instruction

data class LogLine(
    val pc: Int,
    val instructionHex: Int,
    val rdIndex: Int,
    val rs1Index: Int,
    val rs2Index: Int,
    val rdValue: Int,
    val rs1Value: Int,
    val rs2Value: Int,
    val instruction: Instruction
) {
    override fun toString(): String =
        "PC=$pc, " +
            "[$instructionHex], " +
            "x$rdIndex=$rdValue, " +
            "x$rs1Index=$rs1Value, " +
            "x$rs2Index=$rs2Value, " +
            instruction.disassembly
}



