import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

val regex = """[<>^v]""".toRegex()

val allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).map { it.toCharArray() }

var units = mutableListOf<Unit>()

fun main(args: Array<String>) {

    allLines.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            val unit = when (c) {
                'G' -> Unit(x, y, 'G')
                'E' -> Unit(x, y, 'E')
                else -> null
            }

            if (unit != null) {
                units.add(unit)
            }
        }
    }

    var rounds = 0

    mainLoop@ while (true) {
        print()

        units = units.sortedBy { 1000000 * it.y + it.x }.toMutableList()

        var i = 0
        while (i < units.size) {
            val unit = units[i]
            val enemies = units.filter { it.type == unit.type.other() }

            if (enemies.isEmpty()) break@mainLoop
            turn(unit)

            i = units.indexOf(unit) + 1
        }

        rounds++
    }

    print()

    println(rounds)
    val sum = units.map { it.health }.sum()
    println(sum)
    println(sum * rounds)
}

private fun print() {
    allLines.forEach { println(it.fold("") { acc, c -> acc + c }) }
}

fun turn(unit: Unit) {
    if (!canAttack(unit)) {
        val enemies = units.filter { it.type == unit.type.other() }
        var where = enemies.map { it.x to it.y }.toMutableSet()

        var closest = listOf<Pair<Int, Int>>()
        val used = mutableSetOf<Pair<Int, Int>>()
        while (closest.isEmpty() && where.isNotEmpty()) {
            where = where.flatMap { adjacent(it.first, it.second).filter { it.second == '.' }.map { it.first } }
                .toMutableSet()
            where.removeAll(used)
            closest = where.filter { adjacent(unit).any { it1 -> it1.first == it } }
            used.addAll(where)
        }

        val moveTo = closest.sortedBy { 1000000 * it.second + it.first }.firstOrNull()
        if (moveTo != null) {
            allLines[unit.y][unit.x] = '.'

            unit.x = moveTo.first
            unit.y = moveTo.second

            allLines[unit.y][unit.x] = unit.type

            print()
        }
    }

    val inRange = adjacent(unit.x, unit.y)
        .filter { it.second == unit.type.other() }
        .map { units.find { unit1 -> unit1.x to unit1.y == it.first }!! }

    if (inRange.isNotEmpty()) {
        val enemy = inRange.minWith(targetSelector)!!
        enemy.health -= unit.attack
        if (enemy.health <= 0) {
            units.remove(enemy)
            allLines[enemy.y][enemy.x] = '.'
        }
    }
}

val targetSelector = Comparator<Unit> { first, second ->
    val health = first.health - second.health
    if (health != 0) return@Comparator health

    val y = first.y - second.y
    if (y != 0) return@Comparator y

    first.x - second.x
}

fun adjacent(unit: Unit) = adjacent(unit.x, unit.y)

private fun canAttack(unit: Unit): Boolean {
    return adjacent(unit.x, unit.y).any { it.second == unit.type.other() }
}

private fun adjacent(x: Int, y: Int): List<Pair<Pair<Int, Int>, Char>> {
    return listOf(
        x to y - 1, x - 1 to y, x + 1 to y, x to y + 1
    ).map { it to allLines[it.second][it.first] }
}

private fun Char.other(): Char {
    when (this) {
        'G' -> return 'E'
        'E' -> return 'G'
    }
    throw RuntimeException()
}

data class Unit(var x: Int, var y: Int, var type: Char, var health: Int = 200, var attack: Int = 3)
