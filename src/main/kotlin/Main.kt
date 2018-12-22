import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors

val serial = 3031

fun main(args: Array<String>) {
    val (size, max) = IntRange(1, 300)
        .toList().parallelStream()
        .map {
            println("Start $it")
            val pair = it to maxPower(it)
            println("Done $it")
            pair
        }.collect(Collectors.toList())
        .maxBy { it.second.value.maxBy { it.value }!!.value }!!

    //println(cells.map { it.map { it.absoluteValue } + "\n" })
    println(max.index + 1)
    val maxBy = max.value.maxBy { it.value }!!
    println(maxBy.index + 1)
    println(size)


    println()
    println(maxBy.value)
}

private fun maxPower(size: Int): IndexedValue<Iterable<IndexedValue<Int>>> {
    val cells = Array(300) { i ->
        IntArray(300) { j ->
            val x = i + 1
            val y = j + 1
            val rack = x + 10
            val digits = (rack * y + serial) * rack
            val string = digits.toString()
            string.getOrElse(string.length - 3) { '0' }.toString().toInt() - 5
        }
    }

    val max = cells.mapIndexed { i, a ->
        a.mapIndexed { j, it ->
            try {
                sumAll(cells, i, j, size)
            } catch (e: ArrayIndexOutOfBoundsException) {
                0
            }
        }.withIndex()
    }.withIndex().maxBy { it.value.maxBy { it1 -> it1.value }!!.value }!!
    return max
}

private fun sumAll(cells: Array<IntArray>, i: Int, j: Int, size: Int): Int {
    var ret = 0

    for (x in i until i + size) {
        for (y in j until j + size) {
            ret += cells[x][y]
        }
    }

    return ret
}