package entity.instruction

import entity.Processor

sealed class Instruction {
    abstract fun execute(processor: Processor)
    abstract val mnemonic: String
}