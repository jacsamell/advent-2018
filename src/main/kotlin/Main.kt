import org.apache.commons.collections4.list.TreeList

fun main(args: Array<String>) {
    run(459, 71798)
    run(459, 7179800)
}

private fun run(players: Int, max: Int) {
    val marbles = TreeList<Int>().apply { add(0) }
    var index = 0
    var player = 0
    val scores = mutableMapOf<Int, Long>()

    for (i in 1..max) {
        if (i % 23 == 0) {
            var score = scores.getOrDefault(player, 0)
            score += i
            index = index(index - 7, marbles.size)
            score += marbles.removeAt(index)
            scores.put(player, score)
        } else {
            index = index(index + 2, marbles.size)

            marbles.add(index, i)
        }
        player = index(player + 1, players)
    }

    //println(scores)
    //println(scores.entries.maxBy { it.value })
    println(scores.values.max())
}

private fun index(index: Int, max: Int): Int {
    return if (index >= max) index - max else if (index < 0) index + max else index
}