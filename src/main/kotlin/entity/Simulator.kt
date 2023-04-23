package entity

import entity.instruction.BranchInstruction
import entity.instruction.ImmediateInstruction
import entity.instruction.Instruction
import entity.instruction.JumpInstruction
import entity.instruction.RegisterInstruction
import entity.instruction.StoreInstruction
import entity.instruction.UpperInstruction
import extensions.bits
import extensions.opcode
import extensions.rs1
import extensions.rs2
import extensions.toBinaryString
import model.LogLine
import java.io.File
import java.io.PrintWriter


class Simulator(program: ByteArray, outputPath: String) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)

    private val memory = Memory(MEMORY_SIZE)

    private val log = File(outputPath)

    var cycleCount: Int = 0
        private set

    var programCounter = PROGRAM_COUNTER_INITIAL_VALUE
        private set

    init {
        memory.loadProgram(PROGRAM_COUNTER_INITIAL_VALUE, program)
        PrintWriter(log).also {
            it.print("")
            it.close()
        }
    }

    fun run() {
        while (programCounter < PROGRAM_COUNTER_MAX_VALUE) {
            val rawInstruction = memory.loadWord(programCounter)
            val instruction = parseInstruction(rawInstruction)
            executeInstruction(instruction, rawInstruction)
            cycleCount += instruction.cycleCost
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

    fun loadHalf(address: Int) = memory.loadHalf(address)

    fun loadByte(address: Int) = memory.loadByte(address)

    fun storeWord(address: Int, value: Int) = memory.storeWord(address = address, value = value)

    fun storeHalf(address: Int, value: Int) = memory.storeHalf(address = address, value = value)

    fun storeByte(address: Int, value: Byte) = memory.storeByte(address = address, value = value)

    private fun parseInstruction(rawInstruction: Int): Instruction =
        when (rawInstruction.opcode()) {
            IMMEDIATE_INSTRUCTION_OPCODE,
            LOAD_INSTRUCTION_OPCODE,
            JALR_INSTRUCTION_OPCODE -> ImmediateInstruction.fromRawInstruction(rawInstruction)

            STORE_INSTRUCTION_OPCODE -> StoreInstruction(rawInstruction)
            BRANCH_INSTRUCTION_OPCODE -> BranchInstruction(rawInstruction)
            JUMP_INSTRUCTION_OPCODE -> JumpInstruction(rawInstruction)
            LUI_INSTRUCTION_OPCODE -> UpperInstruction(rawInstruction)
            REGISTER_INSTRUCTION_OPCODE -> RegisterInstruction(rawInstruction)
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

        log.appendText(partialLogLine.copy(rdValue = readRegister(rd)).toString() + "\n")
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
        const val MEMORY_SIZE = 10 * 1024 * 1024
        const val PROGRAM_COUNTER_INITIAL_VALUE = 0x100
        const val PROGRAM_COUNTER_MAX_VALUE = 0x20000000

        const val IMMEDIATE_INSTRUCTION_OPCODE = 0b0010011
        const val STORE_INSTRUCTION_OPCODE = 0b0100011
        const val LOAD_INSTRUCTION_OPCODE = 0b0000011
        const val JALR_INSTRUCTION_OPCODE = 0b1100111
        const val BRANCH_INSTRUCTION_OPCODE = 0b1100011
        const val JUMP_INSTRUCTION_OPCODE = 0b1101111
        const val LUI_INSTRUCTION_OPCODE = 0b0110111
        const val REGISTER_INSTRUCTION_OPCODE = 0b0110011
    }
}