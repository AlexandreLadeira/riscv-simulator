package extensions

fun Int.bits(start: Int, end: Int) =
    (this and ((1L shl (end + 1)) - 1).toInt()) shr start

fun Int.withLastBitCleared() = this and (1.inv())

fun Int.toBinaryString() = this.toUInt().toString(radix = 2)
fun Int.toHexString(padding: Int = 8) = this.toUInt().toString(radix = 16).padStart(padding, '0')

fun Int.opcode() = bits(0, 6)
fun Int.rs1() = bits(15, 19)
fun Int.rs2() = bits(20, 24)
fun Int.rd() = bits(7, 11)

fun Int.immediate() = bits(20, 31)
fun Int.storeImmediate() = (bits(25, 31) shl 5) or bits(7, 11)
fun Int.branchImmediate() =
    (bits(31, 31) shl 12) or (bits(7, 7) shl 11) or (bits(25, 30) shl 5) or (bits(8, 11) shl 1)

fun Int.jumpImmediate() =
    (bits(31, 31) shl 20) or (bits(12, 19) shl 12) or (bits(20, 20) shl 11) or (bits(21, 30) shl 1)

fun Int.upperImmediate() = bits(12, 31) and 0xFFFFF
fun Int.shamtImmediate() = bits(20, 24) and 0b11111

fun Int.funct3() = bits(12, 14)
fun Int.funct7() = bits(25, 31)

fun Int.toRegisterName() = "x${this.toString().padStart(2, '0')}"