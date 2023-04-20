import entity.Memory
import entity.Processor
import java.io.File

fun main(args: Array<String>) {

    val program = File("/Users/alexandreladeira/Projects/riscv-simulator/src/main/resources/011.const.bin").readBytes()
    val memory = Memory(ByteArray(1 * 1024 * 1024))
    memory.loadProgram(0x10074, program)
    val processor = Processor(memory)
    processor.run()

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}