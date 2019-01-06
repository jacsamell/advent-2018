import java.nio.file.Files
import java.nio.file.Paths

// y=1595, x=454..478

val regexX = """x=(\d+(?:\.\.\d+)?)""".toRegex()
val regexY = """y=(\d+(?:\.\.\d+)?)""".toRegex()

fun main(args: Array<String>) {
    val lines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    val grid = lines.flatMap {
        val x = regexX.find(it)!!.groupValues[1]
        val y = regexY.find(it)!!.groupValues[1]

        if (x.contains("..")) {
            val range = x.split("..").map { it.toInt() }
            return@flatMap IntRange(range[0], range[1]).map { it to y.toInt() }
        } else {
            val range = y.split("..").map { it.toInt() }
            return@flatMap IntRange(range[0], range[1]).map { x.toInt() to it }
        }
    }.map { it to "#" }.toMap().toMutableMap()

    val minX = grid.keys.minBy { it.first }!!.first
    val maxX = grid.keys.maxBy { it.first }!!.first
    val minY = grid.keys.minBy { it.second }!!.second
    val maxY = grid.keys.maxBy { it.second }!!.second

    print(minY, maxY, minX, maxX, grid)

    var water = mutableListOf<Pair<Int, Int>>()
    var counter = 0
    while (true) {
        val oldWater = water.apply { add(500 to 0) }
        water = mutableListOf()

        val oldGrid = grid.toMap()

        oldWater.forEach {
            val x = it.first
            val y = it.second

            grid.remove(x to y)
            //print(minY, maxY, minX, maxX, grid)

            if (x == maxX || x == minX || y == maxY) return@forEach

            if (!grid.contains(x to y + 1)) {
                water.add(x to y + 1)
                grid[x to y + 1] = "|"
            } else if (!grid.contains(x + 1 to y) || !grid.contains(x - 1 to y)) {
                if (!grid.contains(x + 1 to y)) {
                    water.add(x + 1 to y)
                    grid[x + 1 to y] = "|"
                }
                if (!grid.contains(x - 1 to y)) {
                    water.add(x - 1 to y)
                    grid[x - 1 to y] = "|"
                }
            } else if (
                cannotMove(grid, x, y, oldWater)
            ) {
                grid.put(x to y, "~")
            }

            //print(minY, maxY, minX, maxX, grid)
        }

        print(minY, maxY, minX, maxX, grid)

        println(++counter)

        if (oldGrid == grid) {
            println(grid.filterValues { it == "|" || it == "~" }.size)
            return
        }
    }

}

private fun cannotMove(
    grid: MutableMap<Pair<Int, Int>, String>,
    x: Int,
    y: Int,
    oldWater: MutableList<Pair<Int, Int>>
): Boolean {
    val flows = listOf(x + 1 to y, x - 1 to y, x to y - 1).map { it to (grid[it] ?: ".") }.filter { it.second == "|" }
    if (flows.size < 2) {
        return true
    }

    val index = oldWater.indexOf(x to y)
    return flows.map { oldWater.indexOf(it.first) }.filter { it == index - 1 || it == index + 1 }.size < 2
}

private fun print(
    mnY: Int,
    mxY: Int,
    mnX: Int,
    mxX: Int,
    points: Map<Pair<Int, Int>, String>
) {
    val minX = (points.filterValues { it == "|" || it == "~" }.keys.minBy { it.first }?.first ?: mnX) - 2
    val maxX = (points.filterValues { it == "|" || it == "~" }.keys.maxBy { it.first }?.first ?: mxX) + 2
    val minY = (points.filterValues { it == "|" || it == "~" }.keys.minBy { it.second }?.second ?: mnY) - 2
    val maxY = (points.filterValues { it == "|" || it == "~" }.keys.maxBy { it.second }?.second ?: mxY) + 2

    for (i in minX until 500) print(".")
    print("+")
    for (i in 500 + 1..maxX) print(".")
    println()


    for (j in 1..maxY) {
        for (i in minX..maxX) {
            print(if (points.contains(i to j)) points[i to j] else ".")
        }

        println("$j".padStart(10))
    }

    println()
    println()
}