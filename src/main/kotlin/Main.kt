import entity.Simulator
import java.io.File

fun main(args: Array<String>) {
    val programs = File(args[0]).listFiles { _, name -> name.endsWith(".bin") }?.sortedBy { it.name }
        ?: error("Failed to load programs")

    programs.forEach { program ->
        println("Running program ${program.name}")
        val simulator = Simulator(program.readBytes())
        simulator.run()
    }
}