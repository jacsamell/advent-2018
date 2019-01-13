import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    Main().main()
}

class Main {
    private val lines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))
    var grid = lines.map { it.toCharArray() }.toTypedArray()
    val size = grid.size
    var newGrid = Array(size) { CharArray(size) }

    val all = mutableListOf(grid.map { it.clone() }.toTypedArray())

    fun main() {
        print()
        var time = System.currentTimeMillis()
        for (z in 0 until 1000000000) {
            if (z % 10000 == 0) {
                println("$z / 1000000000")
                println(System.currentTimeMillis() - time)
                time = System.currentTimeMillis()

                print()
            }

            for (y in 0 until grid.size) {
                val row = grid[y]
                for (x in 0 until row.size) {
                    val char = row[x]
                    val new = when (char) {
                        '.' -> if (adjContains('|', 3, x, y)) '|' else '.'
                        '|' -> if (adjContains('#', 3, x, y)) '#' else '|'
                        '#' -> if (adjContains('#', 1, x, y) && adjContains('|', 1, x, y)) '#' else '.'
                        else -> throw RuntimeException()
                    }
                    newGrid[y][x] = new
                }
            }

            val oldGrid = grid
            grid = newGrid
            newGrid = oldGrid

           // print()

            if (all.any { grid.contentDeepEquals(it) }) {

                val (index, match) = all.withIndex().find { grid.contentDeepEquals(it.value) }!!
                //
                println()
                println()

                println(index)
                println(all.size)
                println(all.size - index)
break
                //all.
            } else {
                all.add(grid.map { it.clone() }.toTypedArray())
            }
            println(all.size)

            //print()
        }

        val count = ((1000000000-511) % 28)+511

            val wood = all[count].flatMap { it.asList() }.filter { it == '|' }.size
            val lumber = all[count].flatMap { it.asList() }.filter { it == '#' }.size
            //print()
            println("$wood $lumber")
        println(wood * lumber)

    }

    private fun print() {
        println()
        for (y in 0 until grid.size) {
            val row = grid[y]
            for (x in 0 until row.size) {
                val char = row[x]
                print(char)
            }
            println()
        }
    }

    private fun adjContains(c: Char, count: Int, x: Int, y: Int): Boolean {
        return listOf(
            getOrNull(y - 1, x - 1),
            getOrNull(y - 1, x),
            getOrNull(y - 1, x + 1),
            getOrNull(y, x - 1),
            getOrNull(y, x + 1),
            getOrNull(y + 1, x - 1),
            getOrNull(y + 1, x),
            getOrNull(y + 1, x + 1)
        ).filter { it == c }
            .size >= count
    }

    private fun getOrNull(y: Int, x: Int): Char? {
        return try {
            grid[y][x]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }
}
