package entity

import entity.instruction.BranchInstruction
import entity.instruction.ImmediateInstruction
import entity.instruction.Instruction
import entity.instruction.JumpInstruction
import entity.instruction.StoreInstruction
import extensions.bits
import extensions.opcode
import extensions.rs1
import extensions.rs2
import extensions.toBinaryString
import model.LogLine

class Processor(private val memory: Memory) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)
    var programCounter = PROGRAM_COUNTER_INITIAL_VALUE
        private set

    init {
        // TODO: is that ok?
        writeToRegister(STACK_POINTER_INDEX, STACK_POINTER_INITIAL_VALUE)
    }

    fun run() {
        while (true) {
            val rawInstruction = memory.loadWord(programCounter)
            val instruction = parseInstruction(rawInstruction)
            executeInstruction(instruction, rawInstruction)
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

    fun setPC(i: Int) {
        programCounter = i
    }

    fun loadWord(address: Int) = memory.loadWord(address)

    fun storeWord(address: Int, value: Int) = memory.storeWord(address = address, value = value)

    fun storeByte(address: Int, value: Byte) = memory.storeByte(address = address, value = value)

    private fun parseInstruction(rawInstruction: Int): Instruction =
        when (val opcode = rawInstruction.opcode()) {
            IMMEDIATE_INSTRUCTION_OPCODE,
            LOAD_INSTRUCTION_OPCODE,
            JALR_INSTRUCTION_OPCODE -> ImmediateInstruction(opcode, rawInstruction)

            STORE_INSTRUCTION_OPCODE -> StoreInstruction(rawInstruction)
            BRANCH_INSTRUCTION_OPCODE -> BranchInstruction(rawInstruction)
            JUMP_INSTRUCTION_OPCODE -> JumpInstruction(rawInstruction)
            else -> throw IllegalArgumentException("Unknown instruction: ${rawInstruction.toBinaryString()}")
        }

    private fun executeInstruction(instruction: Instruction, rawInstruction: Int) {
        val rs1 = rawInstruction.rs1()
        val rs2 = rawInstruction.rs2()
        val rd = rawInstruction.bits(7, 11)

        val partialLogLine = LogLine(
            pc = programCounter,
            rawInstruction = rawInstruction,
            rdIndex = rd,
            rs1Index = rs1,
            rs2Index = rs2,
            rdValue = 0,
            rs1Value = readRegister(rs1),
            rs2Value = readRegister(rs2),
            instruction = instruction
        )

        instruction.execute(this)

        println(partialLogLine.copy(rdValue = readRegister(rd)))
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
        const val PROGRAM_COUNTER_INITIAL_VALUE = 0x100
        const val STACK_POINTER_INDEX = 2
        const val STACK_POINTER_INITIAL_VALUE = 0x10000

        const val IMMEDIATE_INSTRUCTION_OPCODE = 0b0010011
        const val STORE_INSTRUCTION_OPCODE = 0b0100011
        const val LOAD_INSTRUCTION_OPCODE = 0b0000011
        const val JALR_INSTRUCTION_OPCODE = 0b1100111
        const val BRANCH_INSTRUCTION_OPCODE = 0b1100011
        const val JUMP_INSTRUCTION_OPCODE = 0b1101111
    }
}