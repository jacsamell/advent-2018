import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

val regex = """(\d+), (\d+)""".toRegex()

fun main(args: Array<String>) {
    val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    val coords = allLines
        .map {
            val vals = regex.find(it)!!.groupValues
            vals[1].toInt() to vals[2].toInt()
        }

    val maxX = coords.maxBy { it.first }!!.first
    val maxY = coords.maxBy { it.second }!!.second
    val minX = coords.minBy { it.first }!!.first
    val minY = coords.minBy { it.second }!!.second

    var answer = 0

    val xRange = minX..maxX
    val yRange = minY..maxY
    for (i in xRange) {
        for (j in yRange) {
            val total = coords.map { abs(it.first - i) + abs(it.second - j) }.reduce { acc, i -> acc + i }
            if (total < 10000) {
                answer++
            }
        }
    }

    println("fin")
    println(answer)
}