import entity.Memory
import entity.Processor

fun main(args: Array<String>) {

    val memory = Memory(ByteArray(1 * 1024 * 1024))
    val processor = Processor(memory)

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}