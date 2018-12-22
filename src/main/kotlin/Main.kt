import java.nio.file.Files
import java.nio.file.Paths

val regex = """([.#]+) => ([.#])""".toRegex()

fun main(args: Array<String>) {
    var start = 0L
    val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    val initial = allLines.removeAt(0).removePrefix("initial state: ")
    allLines.removeAt(0)

    val rules = allLines.map {
        val groupValues = regex.find(it)!!.groupValues
        val rule = groupValues[1].replace(".", "\\.")
        """(?=($rule)).""".toRegex() to groupValues[2]
    }

    var current = initial
    for (i in 1..50000000000) {
        while (!current.startsWith("....")) {
            start--
            current = ".$current"
        }
        while (current.startsWith(".....")) {
            start++
            current = current.substring(1)
        }
        while (!current.endsWith("....")) {
            current = "$current."
        }

        var next = current.replace('#', '.')

        rules.forEach {
            it.first.findAll(current).forEach { match ->
                val index = match.range.first + 2
                next = next.replaceRange(index, index + 1, it.second)
            }
        }

        println(i)
        println(start)
        println(next)

        if (i>10000) {
            start+=50000000001-i
            break
        }

        current = next
    }

    var score = 0L
    current.forEachIndexed { index, c ->
        if (c == '#') {
            score += index + start
        }
    }

    println(score)
}