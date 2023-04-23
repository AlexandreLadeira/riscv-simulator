package entity.instruction

import entity.Simulator

abstract class Instruction {
    abstract fun execute(simulator: Simulator)
    abstract val disassembly: String
    open val cycleCost: Int = 1
}