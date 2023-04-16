package extensions

fun Int.firstByte() = this shr 24
fun Int.firstTwoBytes() = this shr 16
fun Int.firstByteUnsigned() = this ushr 24
fun Int.firstTwoBytesUnsigned() = this ushr 16