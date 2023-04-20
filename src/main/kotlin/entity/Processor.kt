package entity

import entity.instruction.ImmediateInstruction
import entity.instruction.Instruction
import entity.instruction.StoreInstruction
import extensions.bits

class Processor(private val memory: Memory) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)
    private var programCounter = PROGRAM_COUNTER_INITIAL_VALUE

    init {
        // TODO: is that ok?
        writeToRegister(STACK_POINTER_INDEX, STACK_POINTER_INITIAL_VALUE)
    }

    fun run() {
        while (true) {
            val data = memory.loadWord(programCounter)
            val instruction = parseInstruction(data)
            executeInstruction(instruction)
        }
    }

    fun readRegister(register: Int): Int = registers[register]

    fun writeToRegister(register: Int, value: Int) {
        if (register != 0) registers[register] = value
    }

    fun incrementPC() {
        programCounter += 4
    }

    fun addToPC(i: Int) {
        programCounter += i
    }

    fun loadWord(address: Int) = memory.loadWord(address)

    fun storeWord(address: Int, value: Int) = memory.storeWord(address = address, value = value)

    fun storeByte(address: Int, value: Byte) = memory.storeByte(address = address, value = value)

    private fun parseInstruction(data: Int): Instruction =
        when (val opcode = data.bits(0, 6)) {
            IMMEDIATE_INSTRUCTION_OPCODE -> ImmediateInstruction(opcode, data)
            STORE_INSTRUCTION_OPCODE -> StoreInstruction(opcode, data)
            else -> throw IllegalArgumentException("Unknown instruction: $data")
        }

    private fun executeInstruction(instruction: Instruction) {
        println(instruction.disassembly)
        instruction.execute(this)
        // log()
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
        const val PROGRAM_COUNTER_INITIAL_VALUE = 0x10074
        const val STACK_POINTER_INDEX = 2
        const val STACK_POINTER_INITIAL_VALUE = 0x10000

        const val IMMEDIATE_INSTRUCTION_OPCODE = 0b0010011
        const val STORE_INSTRUCTION_OPCODE = 0b0100011
    }
}