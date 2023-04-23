package entity.instruction

import entity.Simulator

sealed class Instruction {
    abstract fun execute(simulator: Simulator)
    abstract val disassembly: String
}