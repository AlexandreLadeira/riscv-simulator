package extensions

private val REGISTER_ABI_NAME = (0..31).associateWith {
    when (it) {
        0 -> "zero"
        1 -> "ra"
        2 -> "sp"
        3 -> "gp"
        4 -> "tp"
        in 5..7 -> "t${it - 5}"
        in 8..9 -> "s${it - 8}"
        in 10..17 -> "a${it - 10}"
        in 18..27 -> "s${it - 16}"
        in 28..31 -> "t${it - 25}"
        else -> error("Invalid register index: $it")
    }
}

val Int.registerABIName: String
    get() = REGISTER_ABI_NAME[this] ?: error("Invalid register index: $this")
