package entity

import entity.instruction.ImmediateInstruction
import entity.instruction.Instruction
import entity.instruction.StoreInstruction
import extensions.bits

class Processor(
    val memory: Memory,
) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)
    private var programCounter = 0x10074

    fun run() {
        // Initialize stack to 0x10000 FIXME!
        writeToRegister(2, 0x10000)

        while (true) {
            val data = memory.loadWord(programCounter)
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

    fun incrementPC() {
        programCounter += 4
    }

    fun addToPC(i: Int) {
        programCounter += i
    }


    private fun parseInstruction(data: Int): Instruction =
        when (val opcode = data.bits(0, 6)) {
            0b0010011 -> ImmediateInstruction(opcode, data)
            0b0100011 -> StoreInstruction(opcode, data)
            else -> throw IllegalArgumentException("Unknown instruction: $data")
        }

    private fun executeInstruction(instruction: Instruction) {
        println(instruction.disassembly)
        instruction.execute(this)
        // log()
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
    }

}