package extensions

fun Int.firstByte() = this shr 24
fun Int.firstTwoBytes() = this shr 16
fun Int.firstByteUnsigned() = this ushr 24
fun Int.firstTwoBytesUnsigned() = this ushr 16

fun Int.bits(start: Int, end: Int) =
    (this and ((1L shl (end + 1)) - 1).toInt()) shr start

fun Int.toBinary() = this.toUInt().toString(radix = 2)