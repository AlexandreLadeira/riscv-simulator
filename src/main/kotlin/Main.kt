import entity.Simulator
import java.io.File

fun main(args: Array<String>) {
    val inputFolder = args[0]
    val outputFolder = args[1]

    val programs = File(inputFolder).listFiles { _, name -> name.endsWith(".bin") }?.sortedBy { it.name }
        ?: error("Failed to load programs")

    programs.forEach { program ->
        println("Running program ${program.name}")
        val simulator = Simulator(
            program = program.readBytes(),
            outputPath = outputFolder + "/" + program.name.replace(".bin", ".log")
        )
        simulator.run()
        println("Finished running program ${program.name} in ${simulator.cycleCount} cycles")
    }
}