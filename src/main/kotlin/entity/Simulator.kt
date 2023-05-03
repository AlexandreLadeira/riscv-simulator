package entity

import entity.instruction.Instruction
import extensions.rd
import extensions.rs1
import extensions.rs2
import model.LogLine
import java.io.File
import java.io.PrintWriter


class Simulator(program: ByteArray, logPath: String) {
    private val registers = IntArray(NUMBER_OF_REGISTERS)

    private val memory = Memory(MEMORY_SIZE)

    private val log = File(logPath)

    private var stopped = false

    var cycleCount: Int = 0
        private set

    var programCounter = PROGRAM_COUNTER_INITIAL_VALUE
        private set

    init {
        memory.loadData(PROGRAM_COUNTER_INITIAL_VALUE, program)
        PrintWriter(log).also {
            it.print("")
            it.close()
        }
    }

    fun run() {
        while (!stopped) {
            val rawInstruction = memory.loadWord(programCounter)
            val instruction = Parser.parseFromRawInstruction(rawInstruction)
            executeInstruction(instruction, rawInstruction)
        }
    }

    fun stop() {
        stopped = true
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

    fun storeHalf(address: Int, value: Short) = memory.storeHalf(address = address, value = value)

    fun storeByte(address: Int, value: Byte) = memory.storeByte(address = address, value = value)

    private fun executeInstruction(instruction: Instruction, rawInstruction: Int) {
        val rs1 = rawInstruction.rs1()
        val rs2 = rawInstruction.rs2()
        val rd = rawInstruction.rd()

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

        cycleCount += instruction.cycleCost
    }

    private companion object {
        const val NUMBER_OF_REGISTERS = 32
        const val MEMORY_SIZE = 10 * 1024 * 1024
        const val PROGRAM_COUNTER_INITIAL_VALUE = 0x1d8
    }
}