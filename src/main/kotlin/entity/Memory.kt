package entity

@OptIn(ExperimentalUnsignedTypes::class)
class Memory(
    private val memory: ByteArray
) {

    fun load(address: Int): Int {

        return TODO()
    }

    fun store(address: Int, value: Int) {
    }

}