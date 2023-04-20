package extensions

val String.mnemonic: String
    get() = padEnd(8, ' ').lowercase()