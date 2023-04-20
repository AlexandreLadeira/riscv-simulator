package entity

class Memory(size: Int) {
    private val memory = ByteArray(size)

    fun loadWord(address: Int): Int = (0..3).fold(0u) { acc, i ->
        acc or (loadByte(address + i).toUByte().toUInt() shl (i * 8))
    }.toInt()

    fun loadByte(address: Int): Byte = memory[address]

    fun storeWord(address: Int, value: Int) {
        (0..3).forEach {
            memory[address + it] = ((value shr (8 * it)) and 0xFF).toByte()
        }
    }

    fun storeByte(address: Int, value: Byte) {
        memory[address] = value
    }

    fun loadProgram(startAddress: Int, program: ByteArray) {
        program.forEachIndexed { index, byte ->
            memory[startAddress + index] = byte
        }
    }

}