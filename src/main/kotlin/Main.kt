import java.nio.file.Files
import java.nio.file.Paths

// y=1595, x=454..478

val regexX = """x=(\d+(?:\.\.\d+)?)""".toRegex()
val regexY = """y=(\d+(?:\.\.\d+)?)""".toRegex()

fun main(args: Array<String>) {
    Main().main()
}

class Main {
    private val lines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))

    private val grid = lines.flatMap {
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

    private val minY = grid.keys.minBy { it.second }!!.second
    private val maxY = grid.keys.maxBy { it.second }!!.second

    val water = mutableListOf<Pair<Int, Int>>()

    private var counter = 0

    fun main() {
        print()
        while (!calculateNextPath()) {
        }
        print()
    }

    private fun calculateNextPath(): Boolean {

        val oldWater = water.toMutableList().apply { add(500 to minY-1) }
        water.clear()

        val oldGrid = grid.toMap()

        oldWater.forEach {
            val x = it.first
            val y = it.second

            grid.remove(x to y)
            //print(minY, maxY, minX, maxX, grid)

            if (grid.blocked(x to y + 1)) {
                if (y < maxY) {
                    water.add(x to y + 1)
                    grid[x to y + 1] = "|"
                }
            } else if (!grid.contains(x + 1 to y) || !grid.contains(x - 1 to y)) {
                if (!grid.contains(x + 1 to y)) {
                    water.add(x + 1 to y)
                    grid[x + 1 to y] = "|"
                }
                if (!grid.contains(x - 1 to y)) {
                    water.add(x - 1 to y)
                    grid[x - 1 to y] = "|"
                }
            } else if (cannotMove(grid, x, y, oldWater)) {
                grid.put(x to y, "~")
            } else {
                water.add(x to y)
                grid.put(x to y, "|")
            }

            //print(minY, maxY, minX, maxX, grid)
        }

        //print()

        //val current = grid.filterValues { it == "|" || it == "~" }.keys.maxBy { it.second }!!.second
        //println("$current / $maxY")
        println("${++counter} of 13635")

        if (oldGrid == grid) {
            println(grid.filterValues { it == "|" || it == "~" }.size)
            return true
        }
        return false
    }

    private fun cannotMove(
        grid: MutableMap<Pair<Int, Int>, String>,
        x: Int,
        y: Int,
        oldWater: MutableList<Pair<Int, Int>>
    ): Boolean {
        val flows =
            listOf(x + 1 to y, x - 1 to y).map { it to (grid[it] ?: ".") }.filter { it.second == "|" }
        // is there a block to the other side
        when {
            flows.size == 2 -> return false
            flows.size == 1 -> {
                var where = flows.single().first
                val inc = when (where) {
                    x + 1 to y -> 1
                    x - 1 to y -> -1
                    else -> 0
                }
                if (inc == 0) return true

                while (grid[where] == "|") {
                    where = where.first + inc to where.second
                }

                // if there is a path out then dont freeze
                return grid[where] != null
            }
            else -> return true
        }
    }

    private fun print() {
        val points = grid
        val minX = (points.filterValues { it == "|" || it == "~" }.keys.minBy { it.first }?.first ?: points.keys.minBy { it.first }?.first!!) - 2
        val maxX = (points.filterValues { it == "|" || it == "~" }.keys.maxBy { it.first }?.first ?: points.keys.maxBy { it.first }?.first!!) + 2
        val minY = (points.filterValues { it == "|" || it == "~" }.keys.minBy { it.second }?.second ?: minY) - 2
        val maxY = (points.filterValues { it == "|" || it == "~" }.keys.maxBy { it.second }?.second ?: maxY) + 2

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
}

private fun <K, V> MutableMap<K, V>.blocked(key: K): Boolean {
    val get = get(key)
    return get == null || get == "|"
}
