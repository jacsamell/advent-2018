import Direction.*
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.random.Random

val regex = """[<>^v]""".toRegex()

fun main(args: Array<String>) {
    var allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data"))


    val map = allLines.map {
        it.replace('<', '-')
            .replace('>', '-')
            .replace('^', '|')
            .replace('v', '|')
    }

    val carts =
        allLines.withIndex().flatMap { regex.findAll(it.value).asIterable().map { result -> it to result } }.map {
            Cart(it.second.range.start, it.first.index, getDir(it.second.value))
        }

    while (true) {
        //print(allLines, carts)


        carts.sortedBy { it.y*1000000 + it.x }.forEach {
            move(map, it)
            val collision = carts.find { carts.find { it1 -> it1.x == it.x && it1.y == it.y && it != it1 } != null }

            if (collision != null) {
                println("${collision.x} ${collision.y}")
                return
            }
        }
    }
}

private fun print(allLines: List<String>, carts: List<Cart>) {
    val allLinesCopy = allLines.toMutableList()
    carts.forEach {
        allLinesCopy[it.y] = allLinesCopy[it.y].replaceRange(
            IntRange(it.x, it.x), when (it.direction) {
                UP -> "^"
                DOWN -> "v"
                LEFT -> "<"
                RIGHT -> ">"
            }
        )
    }
    allLinesCopy.forEachIndexed { index, it ->
        it.forEach { print(it) }
        println(index)
    }
}

private fun move(allLines: List<String>, it: Cart) {
    val direction = it.direction
    val x = it.x + when (direction) {
        LEFT -> -1
        RIGHT -> 1
        else -> 0
    }

    val y = it.y + when (direction) {
        DOWN -> 1
        UP -> -1
        else -> 0
    }

    val next = allLines[y][x]
    val newDir = when (next) {
        '\\' -> when (direction) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> UP
            RIGHT -> DOWN
        }
        '/' -> when (direction) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> DOWN
            RIGHT -> UP
        }
        '+' -> {
            when (it.nextDir()) {
                UP -> direction
                LEFT -> when (direction) {
                    UP -> LEFT
                    LEFT -> DOWN
                    DOWN -> RIGHT
                    RIGHT -> UP
                }
                RIGHT -> when (direction) {
                    UP -> RIGHT
                    LEFT -> UP
                    DOWN -> LEFT
                    RIGHT -> DOWN
                }
                DOWN -> throw RuntimeException()
            }
        }
        '-', '|' -> direction
        else -> throw RuntimeException("$next")
    }

    it.x = x
    it.y = y
    it.direction = newDir
}

fun getDir(value: String): Direction {
    return when (value.toCharArray().single()) {
        '<' -> LEFT
        '>' -> RIGHT
        '^' -> UP
        'v' -> DOWN
        else -> throw RuntimeException()
    }
}

data class Cart(var x: Int, var y: Int, var direction: Direction, var lastDir: Direction = DOWN) {
    fun nextDir(): Direction {
        lastDir = when (lastDir) {
            DOWN, RIGHT -> LEFT
            LEFT -> UP
            UP -> RIGHT
        }
        return lastDir
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
