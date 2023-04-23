package model

import entity.instruction.Instruction
import extensions.toHexString
import extensions.toRegisterName

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
        "PC=${pc.toHexString()} " +
            "[${rawInstruction.toHexString()}] " +
            "${rdIndex.toRegisterName()}=${rdValue.toHexString()} " +
            "${rs1Index.toRegisterName()}=${rs1Value.toHexString()} " +
            "${rs2Index.toRegisterName()}=${rs2Value.toHexString()} " +
            instruction.disassembly

}



