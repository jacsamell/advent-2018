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

    val points = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()

    val xRange = minX..maxX
    val yRange = minY..maxY
    for (i in xRange) {
        for (j in yRange) {
            val minDist = coords.groupBy { abs(it.first - i) + abs(it.second - j) }.minBy { it.key }!!
            if (minDist.value.size == 1) {
                points[i to j] = minDist.value[0]
            } else {
                points[i to j] = null
            }
        }
    }

    val limits =
        xRange.map { it to minY } +
                xRange.map { it to maxY } +
                yRange.map { it to minX } +
                yRange.map { it to maxX }
    val infinite = limits.map { points[it] }

    val filteredPoints = points.entries.groupBy { it.value }
        .minus(null as Pair<Int, Int>?)
        .filterNot { infinite.contains(it.key) }
        .mapKeys { it.key!! }
    val maxBy = filteredPoints
        .maxBy { it.value.size }!!
    val max = maxBy
        .value.size

    for (i in xRange) {
        for (j in yRange) {
            print("${filteredPoints[i to j]}".padEnd(15, ' '))
        }
        println()
    }

    println("fin")
    println(maxBy)
    println(max)
}