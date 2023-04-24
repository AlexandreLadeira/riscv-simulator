package entity

class Memory(size: Int) {
    private val memory = ByteArray(size)

    fun loadWord(address: Int): Int = loadBytes(address, 4).toInt()

    fun loadHalf(address: Int): Short = loadBytes(address, 2).toShort()

    fun loadByte(address: Int): Byte = memory[address]

    fun storeWord(address: Int, value: Int) = storeBytes(address, value, 4)

    fun storeHalf(address: Int, value: Short) = storeBytes(address, value.toInt(), 2)

    fun storeByte(address: Int, value: Byte) {
        memory[address] = value
    }

    fun loadData(startAddress: Int, data: ByteArray) {
        data.copyInto(memory, destinationOffset = startAddress)
    }

    private fun loadBytes(address: Int, n: Int) =
        (0 until n).fold(0u) { acc, i ->
            acc or (loadByte(address + i).toUByte().toUInt() shl (i * 8))
        }

    private fun storeBytes(address: Int, value: Int, n: Int) =
        (0 until n).forEach {
            memory[address + it] = ((value shr (8 * it)) and 0xFF).toByte()
        }


}