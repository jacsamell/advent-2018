import kotlin.math.absoluteValue

val serial = 3031

fun main(args: Array<String>) {
    val cells = Array(301) { x ->
        IntArray(301) { y ->
            val rack = x + 10
            val digits = (rack * y + serial) * rack
            val string = digits.toString()
            string.getOrElse(string.length - 3) { '0' }.toString().toInt() - 5
        }
    }

    val max = cells.mapIndexed { x, a ->
        a.mapIndexed { y, it ->
            try {
                it +
                        cells[x - 1][y - 1] +
                        cells[x - 1][y] +
                        cells[x - 1][y + 1] +
                        cells[x][y - 1] +
                        cells[x][y + 1] +
                        cells[x + 1][y - 1] +
                        cells[x + 1][y] +
                        cells[x + 1][y + 1]
            } catch (e: ArrayIndexOutOfBoundsException) {
                0
            }
        }.withIndex()
    }.withIndex().maxBy { it.value.maxBy { it.value }!!.value }!!

    //println(cells.map { it.map { it.absoluteValue } + "\n" })
    println(max.index)
    println(max.value.maxBy { it.value }!!.index)
}