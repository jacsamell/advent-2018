import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

const val workers = 5

val regex = """Step (.) must be finished before step (.) can begin\.""".toRegex()

var answer = 0
var working = listOf<Pair<Char, Int>>()

fun main(args: Array<String>) {
    val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    var deps = allLines
        .map {
            val vals = regex.find(it)!!.groupValues
            vals[1].toCharArray()[0] to vals[2].toCharArray()[0]
        }
        .groupBy {
            it.second
        }
        .mapValues { it.value.map { it.first } }

    var unblocked = deps.values.flatten().toSet().filter { it: Char -> !deps.containsKey(it) }

    while (unblocked.isNotEmpty() || working.isNotEmpty()) {
        // pick up jobs
        while (working.size < workers && unblocked.isNotEmpty()) {
            val selected = unblocked.sorted().first()

            unblocked -= selected

            working += selected to selected.toTime()
        }

        // increase time
        answer++

        // expire completed jobs
        working = working.map { it.first to it.second - 1 }

        val completed = working.filter { it.second == 0 }.map { it.first }

        working = working.filter { it.second > 0 }

        completed.forEach { c ->
            deps = deps.mapValues { it.value.minus(c) }

            unblocked += deps.filterValues { it.isEmpty() }.map { it.key }

            deps = deps.filterValues { it.isNotEmpty() }
        }
    }

    println("Fin")
    println(answer)
}

private fun Char.toTime() = (toInt() - 'A'.toInt() + 61)