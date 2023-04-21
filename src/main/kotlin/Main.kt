import entity.Memory
import entity.Processor
import java.io.File

fun main(args: Array<String>) {
    val program = File(args[0]).readBytes()
    val memory = Memory(10 * 1024 * 1024)
    memory.loadProgram(0x100, program)
    val processor = Processor(memory)
    processor.run()
}