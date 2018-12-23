import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

val regex = """[<>^v]""".toRegex()

lateinit var allLines: List<CharArray>

lateinit var units: MutableList<Unit>

var elfCount: Int = -1


fun main(args: Array<String>) {
    var i=3
    while (true) {
        allLines = Files.readAllLines(Paths.get("/home/jacob/dev/advent/src/main/kotlin/Data")).map { it.toCharArray() }
        units = mutableListOf()

        val final = run(i)

        println("$final of $elfCount with $i")
        println()

        if (final == elfCount)
            return

        i++
    }
}

private fun run(elfAttack: Int): Int {
    allLines.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            val unit = when (c) {
                'G' -> Unit(x, y, 'G', 3)
                'E' -> Unit(x, y, 'E', elfAttack)
                else -> null
            }

            if (unit != null) {
                units.add(unit)
            }
        }
    }

    if (elfCount == -1)
        elfCount = units.filter { it.type == 'E' }.size

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

    return units.filter { it.type == 'E' }.size
}

private fun print() {
    allLines.forEach { println(it.fold("") { acc, c -> acc + c }) }
}

fun turn(unit: Unit) {
    if (!canAttack(unit)) {
        val enemies = units.filter { it.type == unit.type.other() }.map { it.x to it.y }

        val closestEnemies = getClosest(unit.x to unit.y, enemies)

        val enemyLocation = closestEnemies.sortedBy { 1000000 * it.second + it.first }.firstOrNull()

        if (enemyLocation != null) {
            val shortestPathSteps = getClosest(enemyLocation, listOf(unit.x to unit.y))
            val moveTo = shortestPathSteps
                .sortedBy { 1000000 * it.second + it.first }.first()

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

private fun getClosest(
    start: Pair<Int, Int>,
    end: List<Pair<Int, Int>>
): List<Pair<Int, Int>> {
    var where = mutableSetOf(start)
    val endUp = end.flatMap { adjacent(it.first, it.second) }.filter { it.second == '.' }.map { it.first }

    var closest = listOf<Pair<Int, Int>>()
    val used = mutableSetOf<Pair<Int, Int>>()
    while (closest.isEmpty() && where.isNotEmpty()) {
        closest = where.filter { point -> endUp.any { it == point } }
        used.addAll(where)
        where = where.flatMap { adjacent(it.first, it.second).filter { it.second == '.' }.map { it.first } }
            .toMutableSet()
        where.removeAll(used)
    }
    return closest
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

data class Unit(var x: Int, var y: Int, var type: Char, var attack: Int, var health: Int = 200)
