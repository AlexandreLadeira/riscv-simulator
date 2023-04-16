package entity

import entity.instruction.Instruction

class Processor(
    val memory: Memory,
) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)
    var programCounter = 0

    fun run() {
        while (true) {
            val data = memory.load(programCounter)
            val instruction = parseInstruction(data)
            executeInstruction(instruction)
        }
    }

    fun readRegister(register: Int): Int =
        when (register) {
            0 -> 0
            else -> registers[register]
        }

    fun writeToRegister(register: Int, value: Int) =
        when (register) {
            0 -> {}
            else -> registers[register] = value
        }

    fun incrementProgramCounter() = programCounter++

    private fun parseInstruction(data: Int): Instruction {
        TODO()
    }

    private fun executeInstruction(instruction: Instruction) {
        // log()
        instruction.execute(this)
        // log()
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
    }
}